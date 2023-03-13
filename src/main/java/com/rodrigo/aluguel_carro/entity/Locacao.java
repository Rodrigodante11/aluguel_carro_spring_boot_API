package com.rodrigo.aluguel_carro.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name="locacao" , schema = "bd_aluguel")
public class Locacao {

    @Id
    @Column(name="id", unique = true )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locacao_km", nullable = false)
    private String locacaoKM;
    @Column(precision = 2, nullable = false)
    private Double valor;

    @Column(name = "data_locacao", nullable = false)
    private LocalDate dataLocacao;

    @ManyToOne
    @JoinColumn(name = "automovel_id" , nullable = false)
    private Automovel automovel;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

}
