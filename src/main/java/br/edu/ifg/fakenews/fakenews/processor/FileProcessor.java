package br.edu.ifg.fakenews.fakenews.processor;

import br.edu.ifg.fakenews.fakenews.dto.StatusNews;
import br.edu.ifg.fakenews.fakenews.dto.Tb001News;
import br.edu.ifg.fakenews.fakenews.dto.TextFileData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@StepScope
public class FileProcessor implements ItemProcessor<TextFileData, Tb001News> {

    @Value("#{jobParameters['statusNews']}")
    private String statusNews;

    @Override
    public Tb001News process(TextFileData item) {
        Tb001News tb001News = new Tb001News();
        tb001News.setStatusNews(StatusNews.of(statusNews).getNome());
        tb001News.setNews(item.getNews());
        tb001News.setStatusNewsAlgorithm(StatusNews.NO_PROCESS.getNome());
        log.info("ID FILE: {}", item.getInputSrcFileName());
        tb001News.setIdFile(0);
        return tb001News;
    }
}
