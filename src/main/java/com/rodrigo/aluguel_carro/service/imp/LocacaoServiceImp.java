package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Locacao;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.repository.LocacaoRepository;
import com.rodrigo.aluguel_carro.service.LocacaoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocacaoServiceImp implements LocacaoService {

    private LocacaoRepository locacaoRepository;

    public LocacaoServiceImp(LocacaoRepository locacaoRepository){
        this.locacaoRepository = locacaoRepository;
    }

    @Override
    @Transactional
    public Locacao salvar(Locacao locacao) {
        validar(locacao);
        return locacaoRepository.save(locacao);
    }

    @Override
    @Transactional
    public Locacao atualizar(Locacao locacao) {
        Objects.requireNonNull(locacao.getId());
        validar(locacao);
        return locacaoRepository.save(locacao);
    }

    @Override
    @Transactional
    public void deletar(Locacao locacao) {
        Objects.requireNonNull(locacao.getId());
        locacaoRepository.delete(locacao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Locacao> buscar(Locacao locacaoFiltro) {
            Example<Locacao> example = Example.of(locacaoFiltro,
                    ExampleMatcher.matching()
                            .withIgnoreCase()   // ignorar maisculo e minusculo
                            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

            //.withStringMatcher(ExampleMatcher.StringMatcher.STARTING)); // acha todas com Inicio (rod)
            //.withStringMatcher(ExampleMatcher.StringMatcher.ENDING)); // acha todas com Fim (rod)
            //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT)); // so acha (rodrigo) se escrever o nome inteiro

            return locacaoRepository.findAll(example);
    }

    @Override
    public void validar(Locacao locacao) {
        if(locacao.getLocacaoKM() == null || locacao.getLocacaoKM().trim().equals("")){
            throw new ErroClienteException("Informe o KM da Locação");
        }
        if(locacao.getValor() == 0 || locacao.getValor().isNaN()){
            throw new ErroClienteException("Informe um Valor Valido da Locação");
        }
        if(locacao.getDataLocacao() == null ){
            throw new ErroClienteException("Erro ao tentar Cadastrar a Data de Locao , informe ao Desenvovedor");
        }
        if(locacao.getCliente()== null || locacao.getCliente().getId() ==null){
            throw new ErroClienteException("Informe um Cliente Valido para Locação");
        }
        if(locacao.getAutomovel()== null || locacao.getAutomovel().getId() ==null){
            throw new ErroClienteException("Informe um Automovel Valido para Locação");
        }
    }

    @Override
    public List<Locacao> buscarTodosPorClienteId(Long id) {
        return locacaoRepository.findAllByCliente_Id(id);
    }

    @Override
    public List<Locacao> buscarTodosPorAutomovelId(Long id) {
        return locacaoRepository.findAllByAutomovel_Id(id);
    }

    @Override
    public Optional<Locacao> obterPorId(Long id) {
        return locacaoRepository.findById(id);
    }

    @Override
    public List<Locacao> obterTodos() {
        return locacaoRepository.findAll();
    }
}
