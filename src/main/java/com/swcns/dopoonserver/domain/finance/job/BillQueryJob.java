package com.swcns.dopoonserver.domain.finance.job;

import com.swcns.dopoonserver.domain.card.entity.Bill;
import com.swcns.dopoonserver.domain.card.entity.Card;
import com.swcns.dopoonserver.domain.card.repository.BillRepository;
import com.swcns.dopoonserver.domain.card.repository.CardRepository;
import com.swcns.dopoonserver.domain.user.entity.FintechToken;
import com.swcns.dopoonserver.global.infra.finance.service.OpenFinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class BillQueryJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final OpenFinanceService openFinanceService;
    private final CardRepository cardRepository;
    private final BillRepository billRepository;

    @Bean
    public Job syncBillsJob() {
        return jobBuilderFactory.get("bills-job")
                .start(scrapBills())
                .next(removeDuplicates())
                .next(updateDatabase())
                .build();
    }

    @Bean
    public Step scrapBills() {
        return stepBuilderFactory.get("scrap-bills")
                .tasklet((contribution, chunkContext) -> {
                    Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();

                    List<Card> cards = cardRepository.findAll();
                    List<Bill> bills = new ArrayList<>();

                    cards.forEach(card -> {
                        bills.addAll(openFinanceService.getBillHistoryOf(card, (Date) parameters.get("date_start"), (Date) parameters.get("date_end")));
                    });

                    log.info("{} bills have been found in scrap step.", bills.size());

                    chunkContext.getStepContext().setAttribute("bills", bills);

                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step removeDuplicates() {
        return stepBuilderFactory.get("remove-duplicates")
                .tasklet((contribution, chunkContext) -> {
                    List<Bill> bills = (List<Bill>) chunkContext.getAttribute("bills");

                    List<String> originalCodes = billRepository.findAll().stream().map(it -> it.getBillCode()).collect(Collectors.toList());
                    bills = bills.stream().filter(it ->
                        !originalCodes.contains(it.getBillCode())
                    ).collect(Collectors.toList());

                    chunkContext.getStepContext().setAttribute("bills", bills);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step updateDatabase() {
        return stepBuilderFactory.get("update-db")
                .tasklet((contribution, chunkContext) -> {
                    List<Bill> bills = (List<Bill>) chunkContext.getAttribute("bills");

                    billRepository.saveAll(bills);
                    bills.forEach(bill -> {
                        bill.getPaymentCard().addBill(bill);
                    });

                    return RepeatStatus.FINISHED;
                }).build();
    }
}
