package com.rodrigo.aluguel_carro.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocacaoDTO {

    private Long id;
    private String locacaoKM;
    private Double valor;
    private LocalDate dataLocacao;
    private Long automovel;
    private Long cliente;

}
