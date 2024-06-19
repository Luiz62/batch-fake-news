package br.edu.ifg.fakenews.fakenews.mapper;

import br.edu.ifg.fakenews.fakenews.domain.Tb001News;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TryNewsRowMapper implements RowMapper<Tb001News> {

    @Override
    public Tb001News mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tb001News tb001News = new Tb001News();
        tb001News.setId(rs.getLong("id"));
        tb001News.setStatusNews(rs.getString("status_news"));
        tb001News.setNews(rs.getString("news"));
        return tb001News;
    }
}
