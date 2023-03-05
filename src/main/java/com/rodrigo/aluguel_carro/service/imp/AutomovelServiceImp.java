package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.exceptions.ErroAutomovelException;
import com.rodrigo.aluguel_carro.repository.AutomovelRepository;
import com.rodrigo.aluguel_carro.service.AutomovelService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AutomovelServiceImp implements AutomovelService {

    private AutomovelRepository automovelRepository;

    public AutomovelServiceImp(AutomovelRepository automovelRepository){
        this.automovelRepository= automovelRepository;
    }

    @Override
    @Transactional
    public Automovel salvar(Automovel automovel) { // Salvar um automovel na base de dados
        validar(automovel);
        return automovelRepository.save(automovel);
    }

    @Override
    @Transactional
    public Automovel atualizar(Automovel automovel) {  // Atualizar um automovel na base de dados
        Objects.requireNonNull(automovel.getId());
        validar(automovel);
        return automovelRepository.save(automovel);
    }

    @Override
    @Transactional
    public void deletar(Automovel automovel) {   // Deletar Automovel na base de dados
        Objects.requireNonNull(automovel.getId());
        automovelRepository.delete(automovel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Automovel> buscar(Automovel automovelFiltro) {  // Buscar Automovel na base de dados
        Example<Automovel> example = Example.of(automovelFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()   // ignorar maisculo e minusculo
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        //.withStringMatcher(ExampleMatcher.StringMatcher.STARTING)); // acha todas com Inicio (rod)
        //.withStringMatcher(ExampleMatcher.StringMatcher.ENDING)); // acha todas com Fim (rod)
        //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT)); // so acha (rodrigo) se escrever o nome inteiro

        return automovelRepository.findAll(example);
    }

    @Override
    public Optional<Automovel> obterPorId(Long id) {  // encontrar Automovel por Id
        return automovelRepository.findById(id);
    }

    @Override
    public List<Automovel> obterPorMarca(String marca) {
        return automovelRepository.findAllByMarca(marca);
    }

    @Override
    public List<Automovel>  obterPorModelo(String modelo) {
        return automovelRepository.findAllByModelo(modelo);
    }
    @Override
    public void validar(Automovel automovel) {
        if(automovel.getMarca() == null || automovel.getMarca().trim().equals("")){
            throw new ErroAutomovelException("Infome uma Marca Valida");
        }
        if(automovel.getModelo() == null || automovel.getModelo().trim().equals("")){
            throw new ErroAutomovelException("Infome um Modelo Valida");
        }
        if(automovel.getCor() == null || automovel.getCor().trim().equals("")){
            throw new ErroAutomovelException("Infome uma Cor Valida");
        }
        if(automovel.getPlaca() == null || automovel.getPlaca().trim().equals("")){
            throw new ErroAutomovelException("Infome uma Placa Valida");
        }
        if(automovel.getAno() == null || automovel.getAno().trim().equals("") || automovel.getAno().length() !=4){
            throw new ErroAutomovelException("Infome um Ano Valido");
        }
        if(automovel.getTipoCarro() == null){
            throw new ErroAutomovelException("Infome o Tipo do Automovel");
        }
    }
}
