package com.rodrigo.aluguel_carro.services;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Locacao;
import com.rodrigo.aluguel_carro.repository.LocacaoRepository;
import com.rodrigo.aluguel_carro.service.imp.LocacaoServiceImp;
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
public class LocacaoServiceTest {

    @SpyBean
    LocacaoServiceImp locacaoServiceImp;

    @MockBean
    LocacaoRepository locacaoRepository;

    @Test
    public void deveSalvarUmaLocacao(){
        Locacao locacaoASalvar = Criar.locacao();

        Assertions.assertDoesNotThrow(() -> {

            Locacao locacaoSalvo = Criar.locacao();
            locacaoSalvo.setId(1L);

            Mockito.doNothing().when(locacaoServiceImp).validar(locacaoASalvar);
            Mockito.when(locacaoRepository.save(locacaoASalvar)).thenReturn(locacaoSalvo);


            Locacao locacaoSalvoImp= locacaoServiceImp.salvar(locacaoASalvar);

            assertThat(locacaoSalvoImp).isNotNull();
            assertThat(locacaoSalvoImp.getId()).isEqualTo(locacaoSalvo.getId());

        });

        Mockito.verify(locacaoRepository, Mockito.times(1)).save(locacaoASalvar);
    }

    @Test
    public void deveAtualizarUmaLocacao(){
        Locacao locacao = Criar.locacao();
        locacao.setId(1L);

        Mockito.doNothing().when(locacaoServiceImp).validar(locacao); // mesma classe que estou testando

        locacaoServiceImp.atualizar(locacao);

        Mockito.verify(locacaoRepository, Mockito.times(1)).save(locacao);
    }

    @Test
    public void deveLancarUmErroAtualizarUmaLocacaoSemIdInformado(){
        Locacao locacao = Criar.locacao();

        Mockito.doNothing().when(locacaoServiceImp).validar(locacao); // mesma classe que estou testando

        Assertions.assertThrows(NullPointerException.class, () -> locacaoServiceImp.atualizar(locacao));

        Mockito.verify(locacaoRepository, Mockito.never()).save(locacao);
    }

    @Test
    public void deveDeletarUmLocacao(){
        Locacao locacao = Criar.locacao();
        locacao.setId(1L);
        locacaoServiceImp.deletar(locacao);

        Mockito.verify(locacaoRepository).delete(locacao);
    }
    @Test
    public void deveLancarUmErroAoDeletarUmLocacaoNaoSalva(){
        Locacao locacao = Criar.locacao();

        Assertions.assertThrows(NullPointerException.class, () -> locacaoServiceImp.deletar(locacao));
        Mockito.verify(locacaoRepository, Mockito.never()).delete(locacao);
    }

    @Test
    public void deveObterUmClientePorId(){
        Locacao locacao = Criar.locacao();
        Long id = 1L;
        locacao.setId(id);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o Automovel direto pois nao estou testando esse metodo
        Mockito.when(locacaoRepository.findById(id)).thenReturn(Optional.of(locacao));

        Optional<Locacao> resultado = locacaoServiceImp.obterPorId(id);

        assertThat(resultado.isPresent()).isTrue();

        Mockito.verify(locacaoRepository).findById(id);
    }

    @Test void deveObterLocacoesPorAutomovelId(){
        Locacao locacao = Criar.locacao();
        Long id = 1L;
        locacao.setId(id);

        Mockito.when(locacaoRepository.findAllByAutomovel_Id(id)).thenReturn(List.of(locacao));

        List<Locacao> resultado =  locacaoServiceImp.buscarTodosPorAutomovelId(id);

        assertThat(resultado.get(0).getId()).isEqualTo(id);

    }

    @Test void deveObterLocacoesPorClienteId(){
        Locacao locacao = Criar.locacao();
        Long id = 1L;
        locacao.setId(id);

        Mockito.when(locacaoRepository.findAllByCliente_Id(id)).thenReturn(List.of(locacao));

        List<Locacao> resultado =  locacaoServiceImp.buscarTodosPorClienteId(id);

        assertThat(resultado.get(0).getId()).isEqualTo(id);

    }
}
