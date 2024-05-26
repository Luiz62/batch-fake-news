package br.edu.ifg.fakenews.fakenews.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StatusNews {
    TRUE(0, "TRUE"),
    FALSE(1, "FALSE"),
    PROCESSING(2, "PROCESSING"),
    NO_PROCESS(3, "NO_PROCESS");

    private final Integer id;

    private final String nome;

    public static StatusNews of(String nome) {
        return Arrays.stream(values())
                .filter(x -> x.getNome().equals(nome))
                .findFirst()
                .orElse(NO_PROCESS);
    }
}
