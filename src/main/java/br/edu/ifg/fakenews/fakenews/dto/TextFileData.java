package br.edu.ifg.fakenews.fakenews.dto;

import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

@Data
public class TextFileData {

    private String news;

    private String inputSrcFileName;
}
