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
@Table( name="cliente" , schema = "aluguel_carro")
public class Cliente {
    @Id
    @Column(name="id", unique = true)
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,length = 100)
    private String nome;

    @Column(nullable = false )
    private Integer idade;

    @Column(nullable = false ,length = 100)
    private String email;

    @Column(nullable = false ,length = 20)
    private String cpf;

    @Column(length = 100, name = "endereco_rua")
    private String enderecoRua;

    @Column(name = "endereco_numero")
    private int enderecoNumero;

    @Column(length = 100, name = "endereco_complemento")
    private String enderecoComplemento;

    @Column(nullable = false ,length = 100)
    private String cidade;

    @Column(nullable = false ,length = 100)
    private String estado;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

//    @ManyToMany(mappedBy = "clientes") // esta sendo mapeado pelo clientes la em Automoveis
//    private Set<Automovel> automoveis; // (Set) para  Unicos (List) pra nao valores repetidos
}
