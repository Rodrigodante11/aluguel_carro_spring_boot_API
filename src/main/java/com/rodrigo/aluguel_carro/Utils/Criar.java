package com.rodrigo.aluguel_carro.Utils;

import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.enums.TipoCarro;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Criar {

    public static Automovel automovel(){  // Cria um Automovel usado para testes unitarios
        return Automovel.builder()
                .marca("Ford Mustang")
                .modelo("Shelby GT500")
                .Ano("2005")
                .cor("Branco")
                .placa("1111LL")
                .tipoCarro(TipoCarro.ESPORTIVO)
                .descricao("O Ford Mustang Shelby GT500 é um versão esportiva da série Mustang da Ford. " +
                        "Preparado pela Shelby American o Mustang Shelby GT500 é a versão mais potente de cada geração " +
                        "do Mustang").build();

    }
}
