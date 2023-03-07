package com.rodrigo.aluguel_carro.services;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Log;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.repository.LogRepository;
import com.rodrigo.aluguel_carro.service.imp.LogServiceImp;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // Cria uma instância do banco de dados em memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LogServiceTest {

    @MockBean
    LogRepository logRepository;

    @SpyBean
    LogServiceImp logServiceImp;

    @Test
    public void deveCriarUmLog(){
        Log logASalvar = Criar.log();

        Usuario usuario = Criar.usuario();
        usuario.setId(1L);

        logASalvar.setUsuario(usuario);

        Assertions.assertDoesNotThrow(() -> {

            Log logSalvo = Criar.log();
            logSalvo.setId(1L);

            Mockito.when(logRepository.save(logASalvar)).thenReturn(logSalvo);

            Log logSalvoImp = logServiceImp.criar(logASalvar);

            assertThat(logSalvoImp).isNotNull();
            assertThat(logSalvoImp.getId()).isEqualTo(logSalvo.getId());

        });
    }

    @Test
    public void deveDeletarUmLog(){
        Log log = Criar.log();
        log.setId(1L);

        Usuario usuario = Criar.usuario();

        log.setUsuario(usuario);
        logServiceImp.deletar(log);

        Mockito.verify(logRepository).delete(log);
    }
    @Test
    public void deveLancarUmErroAoDeletarUmLog(){
        Log log = Criar.log();

        Assertions.assertThrows(NullPointerException.class, () -> logServiceImp.deletar(log));

        Mockito.verify(logRepository, Mockito.never()).delete(log);
    }

    @Test
    public void deveObterUmLogPorId(){
        Log log = Criar.log();
        Long id = 1L;
        log.setId(id);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o Locacao direto pois nao estou testando esse metodo
        Mockito.when(logRepository.findById(id)).thenReturn(Optional.of(log));

        Optional<Log> resultado = logServiceImp.encontrarPorId(id);

        assertThat(resultado.isPresent()).isTrue();

        Mockito.verify(logRepository).findById(id);
    }
}
