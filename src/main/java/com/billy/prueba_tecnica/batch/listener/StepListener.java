package com.billy.prueba_tecnica.batch.listener;


import com.billy.prueba_tecnica.domain.StatsProcessing;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class StepListener implements StepExecutionListener {

    /**
     * This method is called before the step starts.
     * It initializes the execution context with default values.
     *
     * @param stepExecution the current step execution
     */

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        long stepWrittenCount = stepExecution.getReadCount();
        long stepSkipCount = stepExecution.getSkipCount();

        int totalProcessed = executionContext.getInt(StatsProcessing.PROCESSED_JSON_COUNT.name(), 0);
        int totalErrors = executionContext.getInt(StatsProcessing.TOTAL_ERRORS.name(), 0);

        executionContext.putInt(StatsProcessing.PROCESSED_JSON_COUNT.name(), totalProcessed + (int) stepWrittenCount);
        executionContext.putInt(StatsProcessing.TOTAL_ERRORS.name(), totalErrors + (int) stepSkipCount);



        return ExitStatus.COMPLETED;
    }
}
