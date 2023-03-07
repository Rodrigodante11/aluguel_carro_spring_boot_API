package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.enums.TipoCarro;
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

import static org.assertj.core.api.Assertions.*;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AutomovelRepositoryTest {

    @Autowired
    AutomovelRepository automovelRepository;

    @Autowired
    TestEntityManager entityManager;

    private Automovel persistirAutomovel(){
        Usuario usuario = Criar.usuario();
        usuario = entityManager.persist(usuario);

        Automovel automovel = Criar.automovel();
        automovel.setUsuario(usuario);

        return automovel;
    }

    @Test
    public void deveSalvarUmAutomovel(){


        Automovel automovel = persistirAutomovel();

        automovel = automovelRepository.save(automovel);

        assertThat(automovel.getId()).isNotNull();
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmAutomovelSemMarca(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Automovel automovel = persistirAutomovel();
            automovel.setMarca(null);
            automovelRepository.save(automovel);
        });
    }

    @Test
    public void deveSalvarUmAutomovelSemOsCamposObrigatorios(){

        Automovel automovel = persistirAutomovel();
        automovel.setDescricao(null);
        automovel.setImagem(null);
        automovel = automovelRepository.save(automovel);

        assertThat(automovel.getId()).isNotNull();
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmAutomovelSemModelo(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Automovel automovel = persistirAutomovel();
            automovel.setModelo(null);
            automovelRepository.save(automovel);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmAutomovelSemCor(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Automovel automovel = persistirAutomovel();
            automovel.setCor(null);
            automovelRepository.save(automovel);
        });
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmAutomovelSemPlaca(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Automovel automovel = persistirAutomovel();
            automovel.setPlaca(null);
            automovelRepository.save(automovel);
        });
    }
    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmAutomovelSemAno(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Automovel automovel = persistirAutomovel();
            automovel.setAno(null);
            automovelRepository.save(automovel);
        });
    }
    @Test
    public void deveDeletarUmAutomovel(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        automovel = entityManager.find(Automovel.class, automovel.getId());

        automovelRepository.delete(automovel);

        Automovel automovelInexistente = entityManager.find(Automovel.class, automovel.getId());

        assertThat(automovelInexistente).isNull();
    }

    @Test void deveAtualizarUmAutomovel(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        automovel.setAno("2010");
        automovel.setMarca("Mustang");
        automovel.setModelo("GT500");
        automovel.setCor("Preto");
        automovel.setPlaca("22222L");
        automovel.setTipoCarro(TipoCarro.CONVERSIVEL);
        automovel.setDescricao("Descricao Editada");
        automovelRepository.save(automovel);

        Automovel automovelAtualizado = entityManager.find(Automovel.class, automovel.getId());

        assertThat(automovelAtualizado.getAno()).isEqualTo("2010");
        assertThat(automovelAtualizado.getMarca()).isEqualTo("Mustang");
        assertThat(automovelAtualizado.getModelo()).isEqualTo("GT500");
        assertThat(automovelAtualizado.getCor()).isEqualTo("Preto");

        assertThat(automovelAtualizado.getTipoCarro()).isEqualTo(TipoCarro.CONVERSIVEL);
        assertThat(automovelAtualizado.getDescricao()).isEqualTo("Descricao Editada");

    }

    @Test
    public void deveBuscarUmAutomovelPorId(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        Optional<Automovel> automovelExistente = automovelRepository.findById(automovel.getId());

        assertThat(automovelExistente.isPresent()).isTrue();

    }
    @Test
    public void deveBuscarUmAutomovelPorModelo(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        List<Automovel> automovelExistente = automovelRepository.findAllByModeloContaining(automovel.getModelo());

        assertThat(automovelExistente.get(0).getId()).isNotNull();

    }
    @Test
    public void deveBuscarMaisDeUmAutomovelPorModelo(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        Automovel automovelNovo = Criar.automovel();
        automovelNovo.setUsuario(automovel.getUsuario());

        entityManager.persist(automovelNovo);

        List<Automovel> automovelExistente = automovelRepository.findAllByModeloContaining(automovel.getModelo());

        assertThat(automovelExistente.get(0).getId()).isNotNull();
        assertThat(automovelExistente.get(1).getId()).isNotNull();

    }

    @Test
    public void deveBuscarUmAutomovelPorMarca(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        Automovel automovelNovo = Criar.automovel();
        automovelNovo.setUsuario(automovel.getUsuario());

        entityManager.persist(automovelNovo);

        List<Automovel> automovelExistente = automovelRepository.findAllByMarcaContaining(automovel.getMarca());

        assertThat(automovelExistente.get(0).getId()).isNotNull();

    }

    @Test
    public void deveBuscarMaisDeUmAutomovelPorMarca(){
        Automovel automovel = persistirAutomovel();
        entityManager.persist(automovel);

        Automovel automovelNovo = Criar.automovel();
        automovelNovo.setUsuario(automovel.getUsuario());

        entityManager.persist(automovelNovo);

        List<Automovel> automovelExistente = automovelRepository.findAllByMarcaContaining(automovel.getMarca());

        assertThat(automovelExistente.get(0).getId()).isNotNull();
        assertThat(automovelExistente.get(1).getId()).isNotNull();
    }
}
