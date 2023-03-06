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

    private Locacao persistirLocacao(Locacao locacao){
        Cliente cliente = Criar.cliente();
        cliente = entityManager.persist(cliente);

        Automovel automovel = Criar.automovel();
        automovel = entityManager.persist(automovel);

        locacao.setCliente(cliente);
        locacao.setAutomovel(automovel);
        return locacao;
    }

    @Test
    public void deveSalvarUmaLocacao(){
        Locacao locacao = Criar.locacao();

        persistirLocacao(locacao);

        locacao = locacaoRepository.save(locacao);

        assertThat(locacao.getId()).isNotNull();
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemCliente(){

        Assertions.assertThrows( Exception.class, ()  -> {

            Locacao locacao = Criar.locacao();
            locacao.setCliente(null);
            locacaoRepository.save(locacao);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemAutomovel(){

        Assertions.assertThrows( Exception.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setAutomovel(null);
            locacaoRepository.save(locacao);
        });
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemLocacaoKM(){

        Assertions.assertThrows( Exception.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setLocacaoKM(null);
            locacaoRepository.save(locacao);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemValor(){

        Assertions.assertThrows( Exception.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setValor(null);
            locacaoRepository.save(locacao);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmaLocacaoSemDtaRegistrada(){

        Assertions.assertThrows(Exception.class, () -> {

            Locacao locacao = Criar.locacao();
            locacao.setDataLocacao(null);
            locacaoRepository.save(locacao);
        });
    }

    @Test
    public void deveDeletarUmaLocacao(){
        Locacao locacao = Criar.locacao();

        persistirLocacao(locacao);

        entityManager.persist(locacao);

        locacao = entityManager.find(Locacao.class, locacao.getId());

        locacaoRepository.delete(locacao);

        Locacao locacaoInexistente = entityManager.find(Locacao.class, locacao.getId());

        assertThat(locacaoInexistente).isNull();
    }

    @Test void deveAtualizarUmaLocacao(){
        Locacao locacao = Criar.locacao();

        persistirLocacao(locacao);
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

        persistirLocacao(locacao);

        entityManager.persist(locacao);

        Optional<Locacao> locacaoExistente = locacaoRepository.findById(locacao.getId());

        assertThat(locacaoExistente.isPresent()).isTrue();

    }

    @Test
    public void deveBuscarMaisDeUmaLocacaoPorCliente(){

        Locacao locacao = Criar.locacao();
        Locacao locacaoNovo = Criar.locacao();

        persistirLocacao(locacao);
        locacaoNovo.setCliente(locacao.getCliente());
        locacaoNovo.setAutomovel(locacao.getAutomovel());

        entityManager.persist(locacao);        // persistindo os dados
        entityManager.persist(locacaoNovo);    // persistindo os dados

        List<Locacao> locacaoExistentes = locacaoRepository.findAllByCliente_Id(locacao.getCliente().getId()); // buscando todos pelo ID do cliente

        assertThat(locacaoExistentes.get(0).getId()).isNotNull();
        assertThat(locacaoExistentes.get(1).getId()).isNotNull();

    }

    @Test
    public void deveBuscarMaisDeUmaLocacaoPorAutomovel(){

        Locacao locacao = Criar.locacao();     // primeira locacao a ser salva
        Locacao locacaoNovo = Criar.locacao(); // segunda locacao a ser salva

        persistirLocacao(locacao);
        locacaoNovo.setCliente(locacao.getCliente());
        locacaoNovo.setAutomovel(locacao.getAutomovel());

        entityManager.persist(locacao);      // persistindo os dados
        entityManager.persist(locacaoNovo); // persistindo os dados

        List<Locacao> locacaoExistente = locacaoRepository.findAllByAutomovel_Id(locacao.getAutomovel().getId());  // buscando todos pelo ID do Automovel

        assertThat(locacaoExistente.get(0).getId()).isNotNull();
        assertThat(locacaoExistente.get(1).getId()).isNotNull();

    }

}
