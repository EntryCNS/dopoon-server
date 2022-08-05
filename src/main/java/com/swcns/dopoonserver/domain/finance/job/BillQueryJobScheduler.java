package com.swcns.dopoonserver.domain.finance.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Component
public class BillQueryJobScheduler {
    private final JobLauncher jobLauncher;
    private final BillQueryJob billQueryJob;

    @PostConstruct
    public void onStartUp() {
        job();
    }

    @Scheduled(cron = "* 0/30 * * * * *")
    public void onSchedule() {
        job();
    }

    public void job() {
        log.info("job starting.. in {}", System.currentTimeMillis());

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -10);

        Calendar endCalendar = Calendar.getInstance();

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date_start", Date.from(startCalendar.toInstant()))
                .addDate("date_end", Date.from(endCalendar.toInstant()))
                .toJobParameters();

        try {
            jobLauncher.run(billQueryJob.syncBillsJob(), jobParameters);
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
