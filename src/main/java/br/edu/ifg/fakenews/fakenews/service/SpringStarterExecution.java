package br.edu.ifg.fakenews.fakenews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpringStarterExecution implements CommandLineRunner {

    private final SpringBatchJobExecution springBatchJobExecution;

    @Override
    public void run(String... args) throws Exception {
        log.info("Args {}", Arrays.toString(args));
        if (args != null  && !Arrays.stream(args).toList().isEmpty()
            && !args[0].isEmpty()) {
            runFile(args);
        }
    }

    private void runFile(String[] args) {

        var dir = args[0];
        log.info("dir: [{}] ", dir);

        var statusNews = args[1];
        log.info("statusNews: [{}] ", statusNews);

        JobExecution jobExecution = springBatchJobExecution.runFile(dir, statusNews);
        log.info("Execucao do arquivo finalizado, status execucao [{}]", jobExecution.getExitStatus()
                .getExitDescription());
    }
}
