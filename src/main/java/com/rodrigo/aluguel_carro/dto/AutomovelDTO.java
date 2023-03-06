package com.rodrigo.aluguel_carro.dto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutomovelDTO {

    private Long id;
    private String marca;
    private String modelo;
    private String cor;
    private String placa;
    private String descricao;
    private String ano;
    private String imagem;
    private String tipoCarro;
}
