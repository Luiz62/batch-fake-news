package br.edu.ifg.fakenews.fakenews.reader;

import br.edu.ifg.fakenews.fakenews.dto.TextFileData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class FileReader {

    @Bean("partitioner")
    @StepScope
    public Partitioner partitioner(@Value("#{jobParameters['dir']}") String dir) {
        log.info("In Partitioner");

        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        partitioner.setResources(getResources(dir));
        partitioner.partition(500);
        return partitioner;
    }

    @Bean
    @StepScope
    @Qualifier("itemReader")
    @DependsOn("partitioner")
    public ItemReader<TextFileData> customFileReader(@Value("#{stepExecutionContext['fileName']}") String filename) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get(new UrlResource(filename).getURI()));
        StringBuilder content = new StringBuilder();

        for (String line : allLines) {
            if (!line.trim().isEmpty()) {
                content.append(line).append(" ");
            }
        }
        TextFileData fileData = new TextFileData(content.toString().trim(), filename);
        return new ListItemReader<>(List.of(fileData));
    }



    private String tryName(String filename) {
        return filename.replace("file:/", "");
    }

    public static Resource[] getResources(String dir) {
        Path dirPath = Paths.get(dir);

        List<String> files = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
            for (Path arquivo : stream) {
                files.add(arquivo.getFileName().toString());
            }
        } catch (IOException e) {
            log.error("Falha ao pegar os nomes do arquivos!", e);
        }

        Resource[] resources = new Resource[files.size()];

        for (int i = 0; i < files.size(); i++) {
            Path filePath = Paths.get(dir, files.get(i));
            log.debug("Full file path: {}", filePath);
            resources[i] = new FileSystemResource(filePath);
        }

        return resources;
    }
}
