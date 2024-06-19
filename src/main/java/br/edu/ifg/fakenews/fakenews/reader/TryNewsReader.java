package br.edu.ifg.fakenews.fakenews.reader;

import br.edu.ifg.fakenews.fakenews.domain.Tb001News;
import br.edu.ifg.fakenews.fakenews.mapper.TryNewsRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class TryNewsReader {

    private static final String QUERY = "SELECT TB001_NEWS.id, TB001_NEWS.status_news, TB001_NEWS.news FROM TB001_NEWS WHERE status_news = CAST(? AS TP001_STATUS_NEWS)";

    @StepScope
    @Bean(name = "tryNewsItemReader")
    public JdbcCursorItemReader<Tb001News> itemReader(DataSource dataSource,
                                                      @Value("#{jobParameters['status']}") String status) {
        JdbcCursorItemReader<Tb001News> reader = new JdbcCursorItemReader<>();
        reader.setRowMapper(new TryNewsRowMapper());
        reader.setDataSource(dataSource);
        reader.setSql(QUERY);
        reader.setPreparedStatementSetter(ps -> ps.setString(1, status));
        reader.setSaveState(true);
        return reader;
    }
}
