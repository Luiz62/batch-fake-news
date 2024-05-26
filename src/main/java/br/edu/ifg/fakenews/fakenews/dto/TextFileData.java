package br.edu.ifg.fakenews.fakenews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

@AllArgsConstructor
@Getter
public class TextFileData {

    private String news;

    private String inputSrcFileName;
}
