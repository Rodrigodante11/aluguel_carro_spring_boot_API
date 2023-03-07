package com.rodrigo.aluguel_carro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.dto.AutomovelDTO;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.exceptions.ErroAutomovelException;
import com.rodrigo.aluguel_carro.repository.AutomovelRepository;
import com.rodrigo.aluguel_carro.service.AutomovelService;
import com.rodrigo.aluguel_carro.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
@WebMvcTest(controllers = AutomovelController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
public class AutomovelControllerTest {

    static final String API= "/api/automovel";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    // classe que nao vou testar apenas amockar
    @MockBean
    AutomovelService automovelService;
    @MockBean
    AutomovelRepository automovelRepository;

    @MockBean
    UsuarioService usuarioService;

    static Automovel automovel = Criar.automovel();
    static Usuario usuario = Criar.usuario();
    static Long id = 1L;

    @BeforeAll
    public static void setUpBeforeClass() {
        automovel.setId(id);
        usuario.setId(id);

        automovel.setUsuario(usuario);
    }
    @Test
    public void deveSalvarUmAutomovel() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.salvar(Mockito.any(Automovel.class))).thenReturn(automovel);
        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.of(usuario));

        String json = new ObjectMapper().writeValueAsString(automovelDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isCreated()) // ESPERO UM STATUS OK e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(automovel.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("marca").value(automovel.getMarca()))
                .andExpect(MockMvcResultMatchers.jsonPath("modelo").value(automovel.getModelo()))
                .andExpect(MockMvcResultMatchers.jsonPath("ano").value(automovel.getAno()))
                .andExpect(MockMvcResultMatchers.jsonPath("cor").value(automovel.getCor()))
                .andExpect(MockMvcResultMatchers.jsonPath("placa").value(automovel.getPlaca()))
                .andExpect(MockMvcResultMatchers.jsonPath("imagem").value(automovel.getImagem()))
                .andExpect(MockMvcResultMatchers.jsonPath("tipoCarro").value(automovel.getTipoCarro().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("descricao").value(automovel.getDescricao()));

    }
    @Test
    public void deveLancarUmBadRequestAoTentarSalvarUmAutomovel() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.salvar(Mockito.any(Automovel.class))).thenThrow(ErroAutomovelException.class);
        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.of(usuario));

        String json = new ObjectMapper().writeValueAsString(automovelDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveAtualizarUmAutomovel() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.atualizar(Mockito.any(Automovel.class))).thenReturn(automovel);
        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.of(usuario));
        Mockito.when(automovelService.obterPorId(id)).thenReturn(Optional.ofNullable(automovel));

        String json = new ObjectMapper().writeValueAsString(automovelDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .put(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(automovel.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("marca").value(automovel.getMarca()))
                .andExpect(MockMvcResultMatchers.jsonPath("modelo").value(automovel.getModelo()))
                .andExpect(MockMvcResultMatchers.jsonPath("ano").value(automovel.getAno()))
                .andExpect(MockMvcResultMatchers.jsonPath("cor").value(automovel.getCor()))
                .andExpect(MockMvcResultMatchers.jsonPath("placa").value(automovel.getPlaca()))
                .andExpect(MockMvcResultMatchers.jsonPath("imagem").value(automovel.getImagem()))
                .andExpect(MockMvcResultMatchers.jsonPath("tipoCarro").value(automovel.getTipoCarro().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("descricao").value(automovel.getDescricao()));

    }

    @Test
    public void deveLancarUmBadRequestAoTentarAtualizarUmAutomovelSemEstarNaBaseDeDados() throws Exception {
        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.atualizar(Mockito.any(Automovel.class))).thenReturn(automovel);
        Mockito.when(automovelService.obterPorId(id)).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(automovelDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .put(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveBuscarUmAutomovelPorId() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.obterPorId(id)).thenReturn(Optional.of(automovel));

        String json = new ObjectMapper().writeValueAsString(automovelDTO);


        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(automovel.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("marca").value(automovel.getMarca()))
                .andExpect(MockMvcResultMatchers.jsonPath("modelo").value(automovel.getModelo()))
                .andExpect(MockMvcResultMatchers.jsonPath("ano").value(automovel.getAno()))
                .andExpect(MockMvcResultMatchers.jsonPath("cor").value(automovel.getCor()))
                .andExpect(MockMvcResultMatchers.jsonPath("placa").value(automovel.getPlaca()))
                .andExpect(MockMvcResultMatchers.jsonPath("imagem").value(automovel.getImagem()))
                .andExpect(MockMvcResultMatchers.jsonPath("tipoCarro").value(automovel.getTipoCarro().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("descricao").value(automovel.getDescricao()));

    }
    @Test
    public void deveLancarErroAoBuscarUmAutomovelPorIdInvalido() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.obterPorId(id)).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(automovelDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void deveDeletarUmAutomovelPorId() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.obterPorId(id)).thenReturn(Optional.of(automovel));
        Mockito.doNothing().when(automovelService).deletar(automovel);

        String json = new ObjectMapper().writeValueAsString(automovelDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void deveLancarUmaExcessaoAoTentarDeletarUmAutomovelPorId() throws Exception {

        AutomovelDTO automovelDTO = Criar.automovelDTO();
        automovelDTO.setUsuario(id);

        Mockito.when(automovelService.obterPorId(id)).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(automovelDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }


}
