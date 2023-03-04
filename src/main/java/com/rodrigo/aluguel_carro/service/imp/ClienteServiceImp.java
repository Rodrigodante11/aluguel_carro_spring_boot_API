package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.repository.ClienteRepository;
import com.rodrigo.aluguel_carro.service.ClienteService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteServiceImp implements ClienteService {

    private ClienteRepository clienteRepository;

    public ClienteServiceImp(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }
    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente atualizar(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        validar(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void deletar(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        clienteRepository.delete(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscar(Cliente clienteFiltro) {
        Example<Cliente> example = Example.of(clienteFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()   // ignorar maisculo e minusculo
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        //.withStringMatcher(ExampleMatcher.StringMatcher.STARTING)); // acha todas com Inicio (rod)
        //.withStringMatcher(ExampleMatcher.StringMatcher.ENDING)); // acha todas com Fim (rod)
        //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT)); // so acha (rodrigo) se escrever o nome inteiro

        return clienteRepository.findAll(example);
    }

    @Override
    public void validar(Cliente cliente) {

    }

    @Override
    public Optional<Cliente> obterPorId(Long id) {
        return clienteRepository.findById(id);
    }
}
