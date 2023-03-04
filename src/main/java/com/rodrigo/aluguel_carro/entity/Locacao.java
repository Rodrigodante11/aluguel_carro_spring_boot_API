package com.rodrigo.aluguel_carro.entity;

import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name="locacao" , schema = "aluguel_carro")
public class Locacao {

    @Id
    @Column(name="id", unique = true )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String locacao_km;
    @Column(precision = 2)
    private double valor;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate data_locacao;

    @ManyToOne
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
