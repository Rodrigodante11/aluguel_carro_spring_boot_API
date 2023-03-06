package com.rodrigo.aluguel_carro.Utils;

import com.rodrigo.aluguel_carro.dto.AutomovelDTO;
import com.rodrigo.aluguel_carro.dto.ClienteDTO;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.enums.TipoCarro;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
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

    public static List<AutomovelDTO> automovel(List<Automovel> listaAutomovel){

        List<AutomovelDTO> collect = listaAutomovel.stream().map(automovel -> {

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

    public static Cliente cliente(ClienteDTO clienteDTO){
        return Cliente.builder()
                .id(clienteDTO.getId())
                .nome(clienteDTO.getNome())
                .idade(clienteDTO.getIdade())
                .email(clienteDTO.getEmail())
                .cpf(clienteDTO.getCpf())
                .enderecoRua(clienteDTO.getEnderecoRua())
                .enderecoComplemento(clienteDTO.getEnderecoComplemento())
                .enderecoNumero(clienteDTO.getEnderecoNumero())
                .cidade(clienteDTO.getCidade())
                .estado(clienteDTO.getEstado())
                .dataCadastro(LocalDate.now() )
                .build();
    }

    public static ClienteDTO cliente(Cliente cliente){
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .idade(cliente.getIdade())
                .email(cliente.getEmail())
                .cpf(cliente.getCpf())
                .enderecoRua(cliente.getEnderecoRua())
                .enderecoComplemento(cliente.getEnderecoComplemento())
                .enderecoNumero(cliente.getEnderecoNumero())
                .cidade(cliente.getCidade())
                .estado(cliente.getEstado())
                .dataCadastro(LocalDate.now() )
                .build();
    }

    public static List<ClienteDTO> cliente(List<Cliente> listaClieste){

        List<ClienteDTO> collect = listaClieste.stream().map(cliente -> {

            ClienteDTO clienteDTO =ClienteDTO.builder()
                    .id(cliente.getId())
                    .nome(cliente.getNome())
                    .idade(cliente.getIdade())
                    .email(cliente.getEmail())
                    .cpf(cliente.getCpf())
                    .enderecoRua(cliente.getEnderecoRua())
                    .enderecoComplemento(cliente.getEnderecoComplemento())
                    .enderecoNumero(cliente.getEnderecoNumero())
                    .cidade(cliente.getCidade())
                    .estado(cliente.getEstado())
                    .dataCadastro(LocalDate.now() )
                    .build();

            return clienteDTO;

        }).collect(Collectors.toList());

        return collect;
    }

}
