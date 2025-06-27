package com.billy.prueba_tecnica.batch.listener;

import com.billy.prueba_tecnica.domain.StatsProcessing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
@Component
@Slf4j
@RequiredArgsConstructor
public class JobListener implements JobExecutionListener {

    /**
     * This method is called before the job starts.
     * It can be used to perform any setup or initialization required for the job.
     *
     * @param jobExecution the JobExecution instance containing job details
     */

    @Override
    public void beforeJob(JobExecution jobExecution) {

        log.info("PROCESSING JSON BATCH STARTED  AT: {}", jobExecution.getCreateTime());
    }

    /**
     * This method is called after the job finishes.
     * It can be used to perform any cleanup or finalization required for the job.
     *
     * @param jobExecution the JobExecution instance containing job details
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        // Logic to execute after the job finishes
       log.info("PROCESSING JSON BATCH  TOTAL PROCESSED: {}", jobExecution.getExecutionContext().getInt(StatsProcessing.PROCESSED_JSON_COUNT.name(), 0));
       log.info("PROCESSING JSON BATCH  ERRORS: {}", jobExecution.getExecutionContext().getInt(StatsProcessing.TOTAL_ERRORS.name(), 0));

       log.info("PROCESSING JSON BATCH ENDED CORRECTLY AT: {}", jobExecution.getEndTime());
        Duration duration = Duration.between(jobExecution.getCreateTime(), jobExecution.getEndTime());
       log.info("PROCESSING JSON BATCH WAS EXECUTED IN : {} MS", duration.toMillisPart() );
    }
}
