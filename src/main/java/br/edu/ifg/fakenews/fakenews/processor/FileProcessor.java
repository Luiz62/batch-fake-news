package br.edu.ifg.fakenews.fakenews.processor;

import br.edu.ifg.fakenews.fakenews.dto.StatusNews;
import br.edu.ifg.fakenews.fakenews.dto.Tb001News;
import br.edu.ifg.fakenews.fakenews.dto.TextFileData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@StepScope
public class FileProcessor implements ItemProcessor<TextFileData, Tb001News> {

    @Value("#{jobParameters['statusNews']}")
    private String statusNews;

    @Override
    public Tb001News process(TextFileData item) {

        Tb001News tb001News = new Tb001News();
        tb001News.setStatusNews(StatusNews.of(statusNews.toUpperCase()).getNome());
        tb001News.setStatusNewsAlgorithm(StatusNews.NO_PROCESS.getNome());
        tb001News.setNews(item.getNews() != null ? item.getNews() : "INVALID TEXT");
        tb001News.setIdFile(extractIdFile(item.getInputSrcFileName()));
        return tb001News;

    }

    public Integer extractIdFile(String filePath) {
        Pattern pattern = Pattern.compile("/([0-9]+)\\.txt$");
        Matcher matcher = pattern.matcher(filePath);

        if (matcher.find()) {
            return Integer.valueOf(matcher.group(1));
        } else {
            log.warn("No match found");
            return null;
        }
    }
}
