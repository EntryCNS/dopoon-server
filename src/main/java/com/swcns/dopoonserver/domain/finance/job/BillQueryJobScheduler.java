package com.swcns.dopoonserver.domain.finance.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "* 0/30 * * * * *")
    public void job() {
        log.info("job starting.. in {}", System.currentTimeMillis());

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, 10);

        Calendar endCalendar = Calendar.getInstance();

        Map<String, JobParameter> arguments = new HashMap<>();
        arguments.put("date_start", new JobParameter(Date.from(startCalendar.toInstant())));
        arguments.put("date_end", new JobParameter(Date.from(endCalendar.toInstant())));

        try {
            jobLauncher.run(billQueryJob.syncBillsJob(), new JobParameters(arguments));
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
