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
@Table( name="usuario" , schema = "bd_aluguel")
public class Usuario {

    @Id
    @Column(name="id", unique = true )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String nome;

    @Column( nullable = false, unique = true)
    private String email;

    @Column( nullable = false)
    private String senha;
    private LocalDate data_cadastro;

    @Column()
    private boolean isAdmin;
}
