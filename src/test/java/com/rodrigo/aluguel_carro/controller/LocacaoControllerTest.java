package com.rodrigo.aluguel_carro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.dto.LocacaoDTO;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.entity.Locacao;
import com.rodrigo.aluguel_carro.exceptions.ErroLocacaoException;
import com.rodrigo.aluguel_carro.repository.LocacaoRepository;
import com.rodrigo.aluguel_carro.service.AutomovelService;
import com.rodrigo.aluguel_carro.service.ClienteService;
import com.rodrigo.aluguel_carro.service.LocacaoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
@WebMvcTest(controllers = LocacaoController.class) // controle que ira ser testado
@AutoConfigureMockMvc
public class LocacaoControllerTest {

    static final String API= "/api/locacao";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    LocacaoService locacaoService;
    @MockBean
    ClienteService clienteService;
    @MockBean
    AutomovelService automovelService;

    @MockBean
    LocacaoRepository locacaoRepository;

    static Locacao locacao = Criar.locacao();
    static Cliente cliente = Criar.cliente();
    static Automovel automovel = Criar.automovel();
    static Long id = 1L;

    @BeforeAll
    public static void setUpBeforeClass() {
        locacao.setId(id);
        cliente.setId(id);
        automovel.setId(id);

        locacao.setCliente(cliente);
        locacao.setAutomovel(automovel);
    }
    @Test
    public void deveSalvarUmaLocacao() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        Mockito.when(locacaoService.salvar(Mockito.any(Locacao.class))).thenReturn(locacao);

        Mockito.when(clienteService.obterPorId(id)).thenReturn( Optional.of(cliente));
        Mockito.when(automovelService.obterPorId(id)).thenReturn( Optional.of(automovel));

        String json = new ObjectMapper().writeValueAsString(locacaolDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isCreated()) // ESPERO UM STATUS Create e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(locacao.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("locacaoKM").value(locacao.getLocacaoKM()))
                .andExpect(MockMvcResultMatchers.jsonPath("valor").value(locacao.getValor()))
                .andExpect(MockMvcResultMatchers.jsonPath("cliente.id").value(locacao.getCliente().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("automovel.id").value(locacao.getAutomovel().getId()));

    }

    @Test
    public void deveBuscarUmaLocacao() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        String json = new ObjectMapper().writeValueAsString(locacaolDTO); // cast para Json

        Mockito.when(locacaoService.obterPorId(id)).thenReturn(Optional.of(locacao));

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(locacao.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("locacaoKM").value(locacao.getLocacaoKM()))
                .andExpect(MockMvcResultMatchers.jsonPath("valor").value(locacao.getValor()))
                .andExpect(MockMvcResultMatchers.jsonPath("cliente").value(locacao.getCliente().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("automovel").value(locacao.getAutomovel().getId()));

    }

    @Test
    public void deveLancarExcessaoBuscarUmaLocacaoInexistente() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        String json = new ObjectMapper().writeValueAsString(locacaolDTO);

        Mockito.when(locacaoService.obterPorId(id)).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void deveLancarExcessaoAoTentarSalvarUmaLocacao() throws Exception {


        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        Mockito.when(locacaoService.salvar(Mockito.any(Locacao.class))).thenThrow(ErroLocacaoException.class);

        Mockito.when(clienteService.obterPorId(id)).thenReturn( Optional.of(cliente));
        Mockito.when(automovelService.obterPorId(id)).thenReturn( Optional.of(automovel));

        String json = new ObjectMapper().writeValueAsString(locacaolDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }
    @Test
    public void deveAtualizarUmaLocacao() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        Mockito.when(locacaoService.obterPorId(id)).thenReturn(Optional.of(locacao));
        Mockito.when(locacaoService.atualizar(Mockito.any(Locacao.class))).thenReturn(locacao);

        Mockito.when(clienteService.obterPorId(id)).thenReturn( Optional.of(cliente));
        Mockito.when(automovelService.obterPorId(id)).thenReturn( Optional.of(automovel));

        String json = new ObjectMapper().writeValueAsString(locacaolDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .put(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(locacao.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("locacaoKM").value(locacao.getLocacaoKM()))
                .andExpect(MockMvcResultMatchers.jsonPath("valor").value(locacao.getValor()))
                .andExpect(MockMvcResultMatchers.jsonPath("cliente.id").value(locacao.getCliente().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("automovel.id").value(locacao.getAutomovel().getId()));

    }
    @Test
    public void deveLancarExcessaoAoAtualizarUmaLocacaoNaoEncontrada() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        Mockito.when(locacaoService.obterPorId(id)).thenReturn(Optional.empty());
        Mockito.when(locacaoService.atualizar(Mockito.any(Locacao.class))).thenReturn(locacao);

        Mockito.when(clienteService.obterPorId(id)).thenReturn( Optional.of(cliente));
        Mockito.when(automovelService.obterPorId(id)).thenReturn( Optional.of(automovel));

        String json = new ObjectMapper().writeValueAsString(locacaolDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .put(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveDeletarLocacao() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        Mockito.when(locacaoService.obterPorId(id)).thenReturn(Optional.of(locacao));
        Mockito.doNothing().when(locacaoService).deletar(locacao);

        String json = new ObjectMapper().writeValueAsString(locacaolDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
    @Test
    public void deveLancarExcessaoAoDeletarLocacaoNaoEncontrado() throws Exception {

        LocacaoDTO locacaolDTO = Criar.locacaoDTO(locacao);

        Mockito.when(locacaoService.obterPorId(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(locacaoService).deletar(locacao);

        String json = new ObjectMapper().writeValueAsString(locacaolDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}
