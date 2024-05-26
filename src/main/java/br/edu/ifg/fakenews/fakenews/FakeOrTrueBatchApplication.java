package br.edu.ifg.fakenews.fakenews;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class FakeOrTrueBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakeOrTrueBatchApplication.class, args);
	}

}
