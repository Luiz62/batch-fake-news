package br.edu.ifg.fakenews.fakenews.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileJob {

    @Bean
    public Job job(@Qualifier("masterStep") Step masterStep, JobRepository jobRepository) {
        return new JobBuilder("importTextFileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(masterStep)
                .build();
    }
}
