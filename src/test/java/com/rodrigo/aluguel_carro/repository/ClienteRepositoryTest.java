package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteRepositoryTest {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmCliente(){
        Cliente cliente = Criar.cliente();

        cliente = clienteRepository.save(cliente);

        assertThat(cliente.getId()).isNotNull();
    }

    @Test
    public void deveSalvarUmClienteSemOsCamposObrigatorios(){

        Cliente cliente = Criar.cliente();
        cliente.setEnderecoRua(null);
        cliente.setEnderecoComplemento(null);

        cliente = clienteRepository.save(cliente);

        assertThat(cliente.getId()).isNotNull();

    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmClienteSemNome(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Cliente cliente = Criar.cliente();
            cliente.setNome(null);
            clienteRepository.save(cliente);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmClienteSemIdade(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Cliente cliente = Criar.cliente();
            cliente.setIdade(null);
            clienteRepository.save(cliente);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmClienteSemEmail(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Cliente cliente = Criar.cliente();
            cliente.setEmail(null);
            clienteRepository.save(cliente);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmClienteSemCPF(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Cliente cliente = Criar.cliente();
            cliente.setCpf(null);
            clienteRepository.save(cliente);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmClienteSemCidade(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Cliente cliente = Criar.cliente();
            cliente.setCidade(null);
            clienteRepository.save(cliente);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmClienteSemEstado(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Cliente cliente = Criar.cliente();
            cliente.setEstado(null);
            clienteRepository.save(cliente);
        });
    }

    @Test
    public void deveDeletarUmCliente(){
        Cliente cliente = Criar.cliente();

        cliente = entityManager.persist(cliente);

        cliente = entityManager.find(Cliente.class, cliente.getId());

        clienteRepository.delete(cliente);

        Cliente clienteInexistente = entityManager.find(Cliente.class, cliente.getId());

        assertThat(clienteInexistente).isNull();
    }

    @Test
    public void deveBuscarUmClintePorId(){
        Cliente cliente = Criar.cliente();
        entityManager.persist(cliente);

        Optional<Cliente> clienteExistente = clienteRepository.findById(cliente.getId());

        assertThat(clienteExistente.isPresent()).isTrue();
    }
    @Test
    public void deveBuscarUmClientePorId(){
        Cliente cliente = Criar.cliente();
        entityManager.persist(cliente);

        Optional<Cliente> clienteExistente = clienteRepository.findById(cliente.getId());

        assertThat(clienteExistente.isPresent()).isTrue();

    }
    @Test
    public void deveBuscarUmClientePorNome(){
        Cliente cliente = Criar.cliente();
        entityManager.persist(cliente);

        List<Cliente> clienteExistente = clienteRepository.findAllByNomeContaining(cliente.getNome());

        assertThat(clienteExistente.get(0).getId()).isNotNull();
    }
    @Test
    public void deveBuscarMaisdeUmClientePorNome(){
        Cliente cliente = Criar.cliente();
        entityManager.persist(cliente);

        Cliente clienteNovo = Criar.cliente();
        entityManager.persist(clienteNovo);

        List<Cliente> clienteExistente = clienteRepository.findAllByNomeContaining(cliente.getNome());

        assertThat(clienteExistente.get(0).getId()).isNotNull();
        assertThat(clienteExistente.get(1).getId()).isNotNull();
    }

}
