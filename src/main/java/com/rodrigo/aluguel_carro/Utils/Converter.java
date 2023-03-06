package com.rodrigo.aluguel_carro.Utils;

import com.rodrigo.aluguel_carro.dto.AutomovelDTO;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.enums.TipoCarro;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Converter {
    public static Automovel automovel(AutomovelDTO automovelDTO){
        return Automovel.builder()
                .id(automovelDTO.getId())
                .marca(automovelDTO.getMarca())
                .modelo(automovelDTO.getModelo())
                .ano(automovelDTO.getAno())
                .cor(automovelDTO.getCor())
                .placa(automovelDTO.getPlaca())
                .imagem(automovelDTO.getImagem())
                .tipoCarro(TipoCarro.valueOf(automovelDTO.getTipoCarro()))
                .descricao(automovelDTO.getDescricao())
                .build();
    }

    public static AutomovelDTO automovel(Automovel automovel){
        return AutomovelDTO.builder()
                .id(automovel.getId())
                .marca(automovel.getMarca())
                .modelo(automovel.getModelo())
                .ano(automovel.getAno())
                .cor(automovel.getCor())
                .placa(automovel.getPlaca())
                .imagem(automovel.getImagem())
                .tipoCarro(automovel.getTipoCarro().name())
                .descricao(automovel.getDescricao())
                .build();
    }

    public static List<AutomovelDTO> automovel(List<Automovel> listAutomovel){

        List<AutomovelDTO> collect = listAutomovel.stream().map(automovel -> {

            AutomovelDTO automovelDTO =AutomovelDTO.builder()
                    .id(automovel.getId())
                    .marca(automovel.getMarca())
                    .modelo(automovel.getModelo())
                    .ano(automovel.getAno())
                    .cor(automovel.getCor())
                    .placa(automovel.getPlaca())
                    .imagem(automovel.getImagem())
                    .tipoCarro(automovel.getTipoCarro().name())
                    .descricao(automovel.getDescricao())
                    .build();

            return automovelDTO;

        }).collect(Collectors.toList());

        return collect;
    }

}
