package com.rodrigo.aluguel_carro.entity;

import com.rodrigo.aluguel_carro.enums.TipoCarro;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name="automovel" , schema = "aluguel_carro")
public class Automovel {

    @Id
    @Column(name="id", unique = true)
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false ,length = 50)
    private String marca;
    @Column(nullable = false,length = 50)
    private String modelo;
    @Column(nullable = false,length = 50)
    private String cor;
    @Column(nullable = false,length = 10)
    private String placa;
    @Column(nullable = true,length = 255)
    private String descricao;
    @Column(nullable = false)
    private String Ano;
    @Column(name="tipo", nullable = false)
    @Enumerated(value =  EnumType.STRING )
    // @Enumerated(value =  EnumType.ORDINAL ) // pega pelo Ordem (0,1)
    private TipoCarro tipoCarro;
    @Column(nullable = true)
    private String imagem;

}
