package br.edu.ifg.fakenews.fakenews.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tb003ProcessedNews {

    private String id;

    private String news;

    private Integer status;
}
