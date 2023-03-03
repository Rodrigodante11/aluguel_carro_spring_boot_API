package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.enums.TipoCarro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

    @Test
    public void deveSalvarUmAutomovel(){
        Automovel automovel = Criar.automovel();

        automovel = automovelRepository.save(automovel);

        assertThat(automovel.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmAutomovel(){
        Automovel automovel = Criar.automovel();
        entityManager.persist(automovel);

        automovel = entityManager.find(Automovel.class, automovel.getId());

        automovelRepository.delete(automovel);

        Automovel automovelInexistente = entityManager.find(Automovel.class, automovel.getId());

        assertThat(automovelInexistente).isNull();
    }

    @Test void deveAtualizarUmAutomovel(){
        Automovel automovel = Criar.automovel();
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
        Automovel automovel = Criar.automovel();
        entityManager.persist(automovel);

        Optional<Automovel> automovelExistente = automovelRepository.findById(automovel.getId());

        assertThat(automovelExistente.get().getId()).isNotNull();

    }
    @Test
    public void deveBuscarUmAutomovelPorModelo(){
        Automovel automovel = Criar.automovel();
        entityManager.persist(automovel);

        List<Automovel> automovelExistente = automovelRepository.findAllByModelo(automovel.getModelo());

        assertThat(automovelExistente.get(0).getId()).isNotNull();

    }
    @Test
    public void deveBuscarUmAutomovelPorMarca(){
        Automovel automovel = Criar.automovel();
        entityManager.persist(automovel);

        List<Automovel> automovelExistente = automovelRepository.findAllByMarca(automovel.getMarca());

        assertThat(automovelExistente.get(0).getId()).isNotNull();

    }

}
