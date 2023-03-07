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
@Table( name="log" , schema = "aluguel_carro")
public class Log {

    @Id
    @Column(name="id", unique = true)
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Column(name = "data_evento")
    private LocalDate dataEvento;

    private String evento;
    private String descricao ;

}
