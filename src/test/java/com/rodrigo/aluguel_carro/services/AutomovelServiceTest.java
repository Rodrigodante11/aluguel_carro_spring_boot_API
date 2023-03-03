package com.rodrigo.aluguel_carro.services;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.repository.AutomovelRepository;
import com.rodrigo.aluguel_carro.service.imp.AutomovelServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AutomovelServiceTest {

    @SpyBean
    AutomovelServiceImp automovelServiceImp;

    @MockBean
    AutomovelRepository automovelRepository;

    @Test
    public void deveSalvarUmAutomovel(){

        Assertions.assertDoesNotThrow(() -> {
            Automovel automovelASalvar = Criar.automovel();

            Mockito.doNothing().when(automovelServiceImp).validar(automovelASalvar);

            Automovel automovelSalvo = Criar.automovel();
            automovelSalvo.setId(1L);

            Mockito.when(automovelRepository.save(automovelASalvar)).thenReturn(automovelSalvo);

            Automovel automovelSalvoImp = automovelServiceImp.salvar(automovelASalvar);

            assertThat(automovelSalvoImp).isNotNull();
            assertThat(automovelSalvoImp.getId()).isEqualTo(automovelSalvo.getId());
            assertThat(automovelSalvoImp.getAno()).isEqualTo(automovelSalvo.getAno());
            assertThat(automovelSalvoImp.getMarca()).isEqualTo(automovelSalvo.getMarca());
            assertThat(automovelSalvoImp.getModelo()).isEqualTo(automovelSalvo.getModelo());
            assertThat(automovelSalvoImp.getCor()).isEqualTo(automovelSalvo.getCor());
            assertThat(automovelSalvoImp.getTipoCarro()).isEqualTo(automovelSalvo.getTipoCarro());
            assertThat(automovelSalvoImp.getDescricao()).isEqualTo(automovelSalvo.getDescricao());

        });
    }

    @Test
    public void deveAtualizarUmAutomovel(){
        Automovel automovel = Criar.automovel();
        automovel.setId(1L);

        Mockito.doNothing().when(automovelServiceImp).validar(automovel); // mesma classe que estou tentando

        automovelServiceImp.atualizar(automovel);

        Mockito.verify(automovelRepository, Mockito.times(1)).save(automovel);
    }

    @Test
    public void deveLancarErroAoAtualizarUmAutomovelsemIdInformado(){
        Automovel automovel = Criar.automovel();

        Assertions.assertThrows(NullPointerException.class, () -> {


            Mockito.doNothing().when(automovelServiceImp).validar(automovel); // mesma classe que estou tentando

            automovelServiceImp.atualizar(automovel);

        });
        Mockito.verify(automovelRepository, Mockito.never()).save(automovel);
    }

    @Test
    public void deveDeletarUmAutomovel(){
        Automovel automovel = Criar.automovel();
        automovel.setId(1L);

        automovelServiceImp.deletar(automovel);

        Mockito.verify(automovelRepository).delete(automovel);
    }

    @Test
    public void deveLancarUmErroAoTentarDeletarUmAutomovelNaoSalvo(){
        Automovel automovel = Criar.automovel();

        Assertions.assertThrows(NullPointerException.class, () -> {

            automovelServiceImp.deletar(automovel);

        });

        Mockito.verify(automovelRepository, Mockito.never()).delete(automovel);
    }

    @Test
    public void deveObterUmAutomovelPorId(){
        Automovel automovel = Criar.automovel();
        Long id = 1l;
        automovel.setId(id);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o Automovel direto pois nao estou testando esse metodo
        Mockito.when(automovelRepository.findById(id)).thenReturn(Optional.of(automovel));

        Optional<Automovel> resultado = automovelServiceImp.obterPorId(id);

        Assertions.assertTrue(resultado.isPresent());

        Mockito.verify(automovelRepository).findById(id);
    }

    @Test
    public void deveBuscarAutomovelPorModelo(){

        Automovel automovel = Criar.automovel();
        automovel.setId(1l);

        List<Automovel> lista = Arrays.asList(automovel); // cast para list

        // quando chamar o metodo(findAll()) retorna a lista
        // mockando pois isso eh teste do repository de lancamento
        Mockito.when(automovelRepository.findAllByModelo(automovel.getModelo())).thenReturn(lista);

        List<Automovel> resultado = automovelServiceImp.obterPorModelo(automovel.getModelo());

        assertThat(automovel).isNotNull();
        assertThat(resultado.toArray()).isEqualTo(lista.toArray());
        assertThat(resultado.size()).isEqualTo(1);

    }

    @Test
    public void deveBuscarAutomovelPorMarca(){

        Automovel automovel = Criar.automovel();
        automovel.setId(1l);

        List<Automovel> lista = Arrays.asList(automovel); // cast para list

        // quando chamar o metodo(findAll()) retorna a lista
        // mockando pois isso eh teste do repository de lancamento
        Mockito.when(automovelRepository.findAllByMarca(automovel.getMarca())).thenReturn(lista);

        List<Automovel> resultado = automovelServiceImp.obterPorMarca(automovel.getMarca());

        assertThat(automovel).isNotNull();
        assertThat(resultado.toArray()).isEqualTo(lista.toArray());
        assertThat(resultado.size()).isEqualTo(1);

    }

}
