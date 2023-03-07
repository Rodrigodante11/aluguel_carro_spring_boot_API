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
    @Column(length = 300)
    private String descricao;
    @Column(nullable = false,length = 4)
    private String ano;
    private String imagem;

    @Column(name="tipo", nullable = false)
    @Enumerated(value =  EnumType.STRING ) // @Enumerated(value =  EnumType.ORDINAL ) // pega pelo Ordem (0,1)
    private TipoCarro tipoCarro;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

//    @ManyToMany
//    @JoinTable(name= "locacao",schema = "aluguel_carro",
//        joinColumns = @JoinColumn(name = "automovel_id", referencedColumnName = "id",nullable = false),
//
//            inverseJoinColumns = @JoinColumn( name = "Cliente_id", referencedColumnName = "id",nullable = false)
//
//
//
//    )
//    private Set<Cliente> clientes;  // (Set) para  Unicos (List) pra nao valores repetidos

}
