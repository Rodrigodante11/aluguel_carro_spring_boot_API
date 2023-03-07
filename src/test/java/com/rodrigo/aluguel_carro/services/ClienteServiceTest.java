package com.rodrigo.aluguel_carro.services;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.repository.ClienteRepository;
import com.rodrigo.aluguel_carro.service.imp.ClienteServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // Cria uma instância do banco de dados em memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteServiceTest {

    @SpyBean
    ClienteServiceImp clienteServiceImp;

    @MockBean
    ClienteRepository clienteRepository;

    @Test
    public void deveSalvarUmCliente(){
        Cliente clienteASalvar = Criar.cliente();

        Assertions.assertDoesNotThrow(() -> {

            Mockito.doNothing().when(clienteServiceImp).validar(clienteASalvar);

            Cliente clienteSalvo = Criar.cliente();
            clienteSalvo.setId(1L);

            Mockito.when(clienteRepository.save(clienteASalvar)).thenReturn(clienteSalvo);

            Cliente clienteSalvoImp= clienteServiceImp.salvar(clienteASalvar);

            assertThat(clienteSalvoImp).isNotNull();
            assertThat(clienteSalvoImp.getId()).isEqualTo(clienteSalvo.getId());

        });

        Mockito.verify(clienteRepository, Mockito.times(1)).save(clienteASalvar);
    }

    @Test
    public void deveAtualizarUmCliente(){
        Cliente cliente = Criar.cliente();
        cliente.setId(1L);

        Mockito.doNothing().when(clienteServiceImp).validar(cliente); // mesma classe que estou testando

        clienteServiceImp.atualizar(cliente);

        Mockito.verify(clienteRepository, Mockito.times(1)).save(cliente);
    }

    @Test
    public void deveLancarErroAoAtualizarUmClientesemIdInformado() {
        Cliente cliente = Criar.cliente();

        Assertions.assertThrows(NullPointerException.class, () -> {

            Mockito.doNothing().when(clienteServiceImp).validar(cliente); // mesma classe que estou testando

            clienteServiceImp.atualizar(cliente);

        });
        Mockito.verify(clienteRepository, Mockito.never()).save(cliente);
    }
    @Test
    public void deveDeletarUmCliente(){
        Cliente cliente = Criar.cliente();
        cliente.setId(1L);
        clienteServiceImp.deletar(cliente);

        Mockito.verify(clienteRepository).delete(cliente);
    }
    @Test
    public void deveLancarUmErroAoDeletarUmClienteNaoSalvo(){
        Cliente cliente = Criar.cliente();
        cliente.setId(1L);

        clienteServiceImp.deletar(cliente);

        Mockito.verify(clienteRepository).delete(cliente);
    }

    @Test
    public void deveObterUmClientePorId(){
        Cliente cliente = Criar.cliente();
        Long id = 1L;
        cliente.setId(id);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o cliente direto pois nao estou testando esse metodo
        Mockito.when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteServiceImp.obterPorId(id);

        Assertions.assertTrue(resultado.isPresent());

        Mockito.verify(clienteRepository).findById(id);
    }

    @Test
    public void deveObterUmClientePorNome(){

        Cliente cliente = Criar.cliente();
        Long id = 1L;
        cliente.setId(id);

        Mockito.when(clienteRepository.findAllByNomeContaining(cliente.getNome())).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteServiceImp.buscarClientesPorNome(cliente.getNome());

        assertThat(resultado.get(0).getId()).isNotNull();

        Mockito.verify(clienteRepository).findAllByNomeContaining(cliente.getNome());
    }

    @Test
    public void deveValidarUmCliente(){

        Assertions.assertDoesNotThrow(() -> {

            Cliente cliente = Criar.cliente();

            clienteServiceImp.validar(cliente);
        });
    }
    @Test
    public void deveLancarExcessaoValidarUmClienteSemNome(){
        Cliente cliente = Criar.cliente();

        Mockito.doNothing().when(clienteServiceImp).validarEmail(cliente);

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setNome(null);

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe um nome Valido", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setNome("");

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe um nome Valido", exception.getMessage());
    }

    @Test
    public void deveLancarExcessaoValidarUmClienteSemIdade(){
        Cliente cliente = Criar.cliente();

        Mockito.doNothing().when(clienteServiceImp).validarEmail(cliente);

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setIdade(null);

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe uma Idade Valida", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setIdade(0);

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe uma Idade Valida", exception.getMessage());
    }
    @Test
    public void deveLancarExcessaoValidarUmClienteSemCPFouInvalido(){
        Cliente cliente = Criar.cliente();

        Mockito.doNothing().when(clienteServiceImp).validarEmail(cliente);

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setCpf(null);

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe CPF Valido", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setCpf("");

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe CPF Valido", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setCpf("123456789");

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe CPF Valido", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setCpf("123456789101112");

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe CPF Valido", exception.getMessage());
    }

    @Test
    public void deveLancarExcessaoValidarUmClienteSemCidade(){
        Cliente cliente = Criar.cliente();

        Mockito.doNothing().when(clienteServiceImp).validarEmail(cliente);

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setCidade(null);

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe uma Cidade Valida", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setCidade("");

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe uma Cidade Valida", exception.getMessage());

    }

    @Test
    public void deveLancarExcessaoValidarUmClienteSemEstado(){
        Cliente cliente = Criar.cliente();

        Mockito.doNothing().when(clienteServiceImp).validarEmail(cliente);

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setEstado(null);

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe um Estado Valido", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setEstado("");

            clienteServiceImp.validar(cliente);
        });

        Assertions.assertEquals("Informe um Estado Valido", exception.getMessage());
    }

    @Test
    public void deveLancarExcessaoValidarUmClienteSemEmail(){
        Cliente cliente = Criar.cliente();

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setEmail(null);

            clienteServiceImp.validarEmail(cliente);
        });

        Assertions.assertEquals("Informe um Email", exception.getMessage());

        exception = Assertions.assertThrows(ErroClienteException.class, () -> {

            cliente.setEmail("");

            clienteServiceImp.validarEmail(cliente);
        });

        Assertions.assertEquals("Informe um Email", exception.getMessage());
    }
    @Test
    public void deveLancarExcessaoValidarUmClienteComEmailJaExistente(){
        Cliente cliente = Criar.cliente();

        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {
            Mockito.when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

            clienteServiceImp.validarEmail(cliente);

        });
        Assertions.assertEquals("Ja existe um Cliente cadastrado com este email", exception.getMessage());

    }
    @Test
    public void deveLancarExcessaoValidarUmClienteComEmailNaoValidadoPelaRegex(){
        Cliente cliente = Criar.cliente();
        cliente.setEmail("augusto");
        Throwable exception = Assertions.assertThrows(ErroClienteException.class, () -> {
            Mockito.when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);

            clienteServiceImp.validarEmail(cliente);

        });
        Assertions.assertEquals("Email Invalido", exception.getMessage());

    }
}
