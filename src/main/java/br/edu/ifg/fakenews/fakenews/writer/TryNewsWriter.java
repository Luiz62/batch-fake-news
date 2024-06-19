package br.edu.ifg.fakenews.fakenews.writer;

import br.edu.ifg.fakenews.fakenews.domain.Tb001News;
import br.edu.ifg.fakenews.fakenews.domain.Tb003ProcessedNews;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TryNewsWriter {

    private static final String QUERY = "INSERT INTO TB003_PROCESSED_NEWS (status, news) VALUES (:status, :news)";

    @Bean(name = "tryNewsItemWriter")
    public JdbcBatchItemWriter<Tb003ProcessedNews> itemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Tb003ProcessedNews> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(QUERY);
        writer.setDataSource(dataSource);
        return writer;
    }

}
