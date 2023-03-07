package com.rodrigo.aluguel_carro.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {

    private Long id;
    private LocalDate dataEvento;
    private String evento;
    private String descricao ;
    private Long usuario;
}
