package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.repository.ClienteRepository;
import com.rodrigo.aluguel_carro.service.ClienteService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Optional<Cliente> obterPorId(Long id) {

        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> buscarClientesPorNome(String nome) {
        return clienteRepository.findAllByNomeContaining(nome);
    }

    @Override
    public void validarEmail(Cliente cliente) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(cliente.getEmail());

        boolean existe = clienteRepository.existsByEmail(cliente.getEmail());

        if(existe){
            throw new ErroClienteException("Ja existe um Cliente cadastrado com este email");
        }
        if (!matcher.matches()) {
            throw new ErroClienteException("Email Invalido");
        }
        if(cliente.getEmail() == null || cliente.getEmail().trim().equals("")){
            throw new ErroClienteException("Informe um Email");
        }
    }

    @Override
    public void validar(Cliente cliente) {

        if(cliente.getNome() == null || cliente.getNome().trim().equals("")){
            throw new ErroClienteException("Informe um nome Valido");
        }
        if(cliente.getIdade() == null || cliente.getIdade() == 0){
            throw new ErroClienteException("Informe uma Idade Valida");
        }
        if(cliente.getCpf() == null || cliente.getCpf().trim().equals("") || cliente.getCpf().length() != 11){
            throw new ErroClienteException("Informe CPF Valido");
        }
        if(cliente.getCidade() == null || cliente.getCidade().trim().equals("")){
            throw new ErroClienteException("Informe uma Cidade Valida");
        }
        if(cliente.getEstado() == null || cliente.getEstado().trim().equals("")){
            throw new ErroClienteException("Informe um Estado Valido");
        }
        validarEmail(cliente);
    }
}
