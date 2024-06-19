package br.edu.ifg.fakenews.fakenews.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tb001News {
    private Long id;

    private String statusNews;

    private String news;

    private Integer idFile;
}
