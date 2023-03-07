package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Locacao;
import com.rodrigo.aluguel_carro.entity.Log;
import com.rodrigo.aluguel_carro.entity.Usuario;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LogRepositoryTest {

    @Autowired
    LogRepository logRepository;

    @Autowired
    TestEntityManager entityManager;

    private Log log = Criar.log();
    private void persistirUsuarioParaLog(Log log){
        Usuario usuario = Criar.usuario();
        usuario = entityManager.persist(usuario);

        log.setUsuario(usuario);
    }

    @Test
    public void deveCriarUmLog(){

        persistirUsuarioParaLog(log);

        log = logRepository.save(log);

        assertThat(log.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLog(){

        persistirUsuarioParaLog(log);
        entityManager.persist(log);

        Log logEncontrado = entityManager.find(Log.class, log.getId());

        logRepository.delete(logEncontrado);

        Log logInexistente = entityManager.find(Log.class, log.getId());

        assertThat(logInexistente).isNull();
    }

    @Test
    public void deveBuscarUmLogPorId(){

        persistirUsuarioParaLog(log);

        entityManager.persist(log);

        Optional<Log> locacaoExistente = logRepository.findById(log.getId());

        assertThat(locacaoExistente.isPresent()).isTrue();

    }

    @Test
    public void deveBuscarTodosLogs(){

        persistirUsuarioParaLog(log);

        Log logNovo = Criar.log();
        logNovo.setUsuario(log.getUsuario());

        Log logNovoDois = Criar.log();
        logNovoDois.setUsuario(log.getUsuario());

        entityManager.persist(log);
        entityManager.persist(logNovo);
        entityManager.persist(logNovoDois);

        List<Log> locacaoExistente = logRepository.findAll();

        assertThat(locacaoExistente.get(0).getId()).isNotNull();
        assertThat(locacaoExistente.get(1).getId()).isNotNull();
        assertThat(locacaoExistente.get(0).getId()).isNotNull();

    }
}
