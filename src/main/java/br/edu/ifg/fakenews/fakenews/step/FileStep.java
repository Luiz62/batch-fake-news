package br.edu.ifg.fakenews.fakenews.step;

import br.edu.ifg.fakenews.fakenews.domain.Tb001News;
import br.edu.ifg.fakenews.fakenews.dto.TextFileData;
import br.edu.ifg.fakenews.fakenews.processor.FileProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FileStep {

    @Bean(name = "step1")
    public Step step1(
            ItemReader<TextFileData> itemReader,
            FileProcessor processor,
            JdbcBatchItemWriter<Tb001News> writer,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepFile", jobRepository)
                .<TextFileData, Tb001News>chunk(1, transactionManager)
                .processor(processor)
                .writer(writer)
                .reader(itemReader)
                .taskExecutor(new SimpleAsyncTaskScheduler())
                .build();
    }

    @Bean
    @Qualifier("masterStep")
    public Step masterStep(JobRepository jobRepository, @Qualifier("step1") Step step1,
                           @Qualifier("partitioner") Partitioner partitioner) {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("stepPartitioner", partitioner)
                .step(step1)
                .taskExecutor(new SimpleAsyncTaskScheduler())
                .build();
    }
}
