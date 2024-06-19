package br.edu.ifg.fakenews.fakenews.step;

import br.edu.ifg.fakenews.fakenews.domain.Tb001News;
import br.edu.ifg.fakenews.fakenews.domain.Tb003ProcessedNews;
import br.edu.ifg.fakenews.fakenews.dto.TextFileData;
import br.edu.ifg.fakenews.fakenews.processor.FileProcessor;
import br.edu.ifg.fakenews.fakenews.processor.TryNewsProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TryNewsStep {

    @JobScope
    @Bean(name = "stepTryNews")
    public Step tryNewsStep(
            @Qualifier("tryNewsItemReader") JdbcCursorItemReader<Tb001News> itemReader,
            TryNewsProcessor processor,
            @Qualifier("tryNewsItemWriter") JdbcBatchItemWriter<Tb003ProcessedNews> writer,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepTryNews", jobRepository)
                .<Tb001News, Tb003ProcessedNews>chunk(1000, transactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
