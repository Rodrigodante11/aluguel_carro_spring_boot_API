package com.rodrigo.aluguel_carro.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nome;
    private Integer idade;
    private String email;
    private String cpf;
    private String enderecoRua;
    private int enderecoNumero;
    private String enderecoComplemento;
    private String cidade;
    private String estado;
    private LocalDate dataCadastro;

}
