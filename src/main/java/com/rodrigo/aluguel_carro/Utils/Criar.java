package com.rodrigo.aluguel_carro.Utils;

import com.rodrigo.aluguel_carro.dto.*;
import com.rodrigo.aluguel_carro.entity.*;
import com.rodrigo.aluguel_carro.enums.TipoCarro;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;


@UtilityClass
public class Criar {

    public  static Usuario usuario(){
        return Usuario.builder()
                .nome("Rodrigo Augusto de Test")
                .email("teste@gmail.com")
                .senha("12356")
                .isAdmin(true)
                .data_cadastro(LocalDate.now())
                .build();
    }

    public  static UsuarioDTO usuarioDTO(){
        return UsuarioDTO.builder()
                .nome("Rodrigo Augusto de Test")
                .email("teste@gmail.com")
                .senha("12356")
                .admin(true)
                .build();
    }

    public static Automovel automovel(){  // Cria um Automovel usado para testes unitarios
        return Automovel.builder()
                .marca("Ford Mustang")
                .modelo("Shelby GT500")
                .ano("2005")
                .cor("Branco")
                .placa("1111LL")
                .imagem("img/mustang.jpg")
                .tipoCarro(TipoCarro.ESPORTIVO)
                .descricao("O Ford Mustang Shelby GT500 é um versão esportiva da série Mustang da Ford. " +
                        "Preparado pela Shelby American o Mustang Shelby GT500 é a versão mais potente de cada geração " +
                        "do Mustang").build();

    }

    public static AutomovelDTO automovelDTO(){  // Cria um Automovel usado para testes unitarios
        return AutomovelDTO.builder()
                .marca("Ford Mustang")
                .modelo("Shelby GT500")
                .ano("2005")
                .cor("Branco")
                .placa("1111LL")
                .imagem("img/mustang.jpg")
                .tipoCarro("ESPORTIVO")
                .descricao("O Ford Mustang Shelby GT500 é um versão esportiva da série Mustang da Ford. " +
                        "Preparado pela Shelby American o Mustang Shelby GT500 é a versão mais potente de cada geração " +
                        "do Mustang")
                .build();

    }
    public static Cliente cliente(){
        return Cliente.builder()
                .nome("Rodrigo Augusto de Oliveira")
                .idade(20)
                .email("rodrigoaugusto839@gmail.com")
                .cpf("12345678944")
                .enderecoRua("Gilberto Viana")
                .enderecoNumero(1111)
                .enderecoComplemento("Casa")
                .cidade("Paraisopolis")
                .estado("MG")
                .dataCadastro(LocalDate.now())
                .build();
    }
    public static ClienteDTO clienteDTO(){
        return ClienteDTO.builder()
                .nome("Rodrigo Augusto de Oliveira")
                .idade(20)
                .email("rodrigoaugusto839@gmail.com")
                .cpf("12345678944")
                .enderecoRua("Gilberto Viana")
                .enderecoNumero(1111)
                .enderecoComplemento("Casa")
                .cidade("Paraisopolis")
                .estado("MG")
                .build();
    }

    public static Locacao locacao(){
        return Locacao.builder()
                .locacaoKM("5000KM")
                .valor(10000.0)
                .dataLocacao(LocalDate.now())
                .build();
    }

    public static LocacaoDTO locacaoDTO(Locacao locacao){
        return LocacaoDTO.builder()
                .locacaoKM(locacao.getLocacaoKM())
                .valor(locacao.getValor())
                .cliente(locacao.getCliente().getId())
                .automovel(locacao.getAutomovel().getId())
                .build();
    }

    public static Log log(){
        return Log.builder()
                .evento("deletou")
                .dataEvento(LocalDate.now())
                .descricao("Automovel do modelo tal ")
                .build();
    }
    public static LogDTO logDTO(){
        return LogDTO.builder()
                .evento("deletou")
                .descricao("Automovel do modelo tal ")
                .build();
    }
}
