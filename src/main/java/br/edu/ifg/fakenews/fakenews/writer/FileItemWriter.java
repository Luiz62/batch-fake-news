package br.edu.ifg.fakenews.fakenews.writer;

import br.edu.ifg.fakenews.fakenews.dto.Tb001News;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FileItemWriter {

    @Bean
    public JdbcBatchItemWriter<Tb001News> itemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Tb001News> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO tb001_news (status_news, news, id_file) " +
                "VALUES (CAST(:statusNews AS TP001_STATUS_NEWS), :news, :idFile)");
        writer.setDataSource(dataSource);
        return writer;
    }

}
