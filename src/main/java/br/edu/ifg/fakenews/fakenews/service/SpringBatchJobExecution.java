package br.edu.ifg.fakenews.fakenews.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringBatchJobExecution {

    private final Job jobFile;
    private final Job jobTryNews;

    private final JobLauncher jobLauncher;

    public SpringBatchJobExecution(@Qualifier("jobFile") Job jobFile,
                                   @Qualifier("jobTryNews") Job jobTryNews,
                                   JobLauncher jobLauncher) {
        this.jobFile = jobFile;
        this.jobTryNews = jobTryNews;
        this.jobLauncher = jobLauncher;
    }

    public JobExecution runFile(String dir, String statusNews) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("jobId", System.currentTimeMillis())
                .addString("statusNews", statusNews, false)
                .addString("dir", dir, false)
                .toJobParameters();

        try {
            return jobLauncher.run(jobFile, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Falha ao executar job!", e);
        }
        return null;
    }

    public JobExecution runRotina(String status) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("jobId", System.currentTimeMillis())
                .addString("status", status, false)
                .toJobParameters();

        try {
            return jobLauncher.run(jobTryNews, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Falha ao executar job!", e);
        }
        return null;
    }
}
