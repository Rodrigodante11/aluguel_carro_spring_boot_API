package com.rodrigo.aluguel_carro.dto;

import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Cliente;
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
    private Automovel automovel;
    private Cliente cliente;

}
