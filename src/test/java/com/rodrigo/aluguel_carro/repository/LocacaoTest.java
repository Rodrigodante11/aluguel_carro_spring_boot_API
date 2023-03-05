package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.entity.Locacao;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocacaoTest {

    @Autowired
    LocacaoRepository locacaoRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmaLocacao(){
        Locacao locacao = Criar.locacao();

        locacao = locacaoRepository.save(locacao);

        assertThat(locacao.getId()).isNotNull();
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemCliente(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setCliente(null);
            locacaoRepository.save(locacao);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemAutomovel(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setAutomovel(null);
            locacaoRepository.save(locacao);
        });
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemLocacaoKM(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setLocacaoKM(null);
            locacaoRepository.save(locacao);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemValor(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setValor(null);
            locacaoRepository.save(locacao);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemDtaRegistrada(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setDataLocacao(null);
            locacaoRepository.save(locacao);
        });
    }

    @Test
    public void deveDeletarUmaLocacao(){
        Locacao locacao = Criar.locacao();
        entityManager.persist(locacao);

        locacao = entityManager.find(Locacao.class, locacao.getId());

        locacaoRepository.delete(locacao);

        Locacao locacaoInexistente = entityManager.find(Locacao.class, locacao.getId());

        assertThat(locacaoInexistente).isNull();
    }

    @Test void deveAtualizarUmaLocacao(){
        Locacao locacao = Criar.locacao();
        entityManager.persist(locacao);

        locacao.setValor(5555.0);
        locacao.setLocacaoKM("50kM");
        locacao.setDataLocacao(LocalDate.parse("2022-02-02"));
        locacaoRepository.save(locacao);

        Locacao locacaoAtualizado = entityManager.find(Locacao.class, locacao.getId());

        assertThat(locacaoAtualizado.getValor()).isEqualTo(5555.0);
        assertThat(locacaoAtualizado.getLocacaoKM()).isEqualTo("50kM");
        assertThat(locacaoAtualizado.getDataLocacao()).isEqualTo(LocalDate.parse("2022-02-02"));

    }
    @Test
    public void deveBuscarUmaLocacaoPorId(){
        Locacao locacao = Criar.locacao();
        entityManager.persist(locacao);

        Optional<Locacao> locacaoExistente = locacaoRepository.findById(locacao.getId());

        assertThat(locacaoExistente.isPresent()).isTrue();

    }

    @Test
    public void deveBuscarMaisDeUmaLocacaoPorCliente(){
        Cliente cliente = Criar.cliente();     // Cliente para usar o ID de busca

        Locacao locacao = Criar.locacao();     // primeira locacao a ser salva
        Locacao locacaoNovo = Criar.locacao(); // segunda locacao a ser salva

        locacaoNovo.setCliente(cliente);       // setando o cliente que ira ser buscado
        locacao.setCliente(cliente);           // setando o cliente que ira ser buscado

        entityManager.persist(locacao);        // persistindo os dados
        entityManager.persist(locacaoNovo);    // persistindo os dados

        List<Locacao> locacaoExistente = locacaoRepository.findAllByCliente_Id(cliente.getId()); // buscando todos pelo ID do cliente

        assertThat(locacaoExistente.get(0).getId()).isNotNull();
        assertThat(locacaoExistente.get(1).getId()).isNotNull();

    }

    @Test
    public void deveBuscarMaisDeUmaLocacaoPorAutomovel(){
        Automovel automovel = Criar.automovel();  // automovel para usar o ID de busca

        Locacao locacao = Criar.locacao();     // primeira locacao a ser salva
        Locacao locacaoNovo = Criar.locacao(); // segunda locacao a ser salva

        Cliente clienteComEmailDiferente = Criar.cliente();
        clienteComEmailDiferente.setEmail("RodrigoNovoTest@gmail.com");  // 2 e-mais iguais geram erro no sistema de clientes

        locacao.setAutomovel(automovel);     // setando o automovel que ira ser buscado
        locacaoNovo.setAutomovel(automovel); // setando o automovel que ira ser buscado
        locacaoNovo.setCliente(clienteComEmailDiferente);

        entityManager.persist(locacao);      // persistindo os dados
        entityManager.persist(locacaoNovo); // persistindo os dados

        List<Locacao> locacaoExistente = locacaoRepository.findAllByAutomovel_Id(automovel.getId());  // buscando todos pelo ID do Automovel

        assertThat(locacaoExistente.get(0).getId()).isNotNull();
        assertThat(locacaoExistente.get(1).getId()).isNotNull();

    }

}
