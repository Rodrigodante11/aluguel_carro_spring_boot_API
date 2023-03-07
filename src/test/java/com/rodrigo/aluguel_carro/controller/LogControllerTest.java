package com.rodrigo.aluguel_carro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.dto.LogDTO;
import com.rodrigo.aluguel_carro.entity.Log;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.exceptions.ErroLogException;
import com.rodrigo.aluguel_carro.repository.LogRepository;
import com.rodrigo.aluguel_carro.service.LogService;
import com.rodrigo.aluguel_carro.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
@WebMvcTest(controllers = LogController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
public class LogControllerTest {

    static final String API= "/api/log";

    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    LogService logService;

    @MockBean
    LogRepository logRepository;

    @MockBean
    UsuarioService usuarioService;

    static Log log = Criar.log();
    static Usuario usuario = Criar.usuario();
    static Long id = 1L;

    @BeforeAll
    public static void setUpBeforeClass() {
        log.setId(id);
        usuario.setId(id);

        log.setUsuario(usuario);
    }

    @Test
    public void deveCriarUmLog() throws Exception {

        LogDTO logDTO = Criar.logDTO();
        logDTO.setUsuario(id);

        Mockito.when(logService.criar(Mockito.any(Log.class))).thenReturn(log);
        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.of(usuario));

        String json = new ObjectMapper().writeValueAsString(logDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isCreated()) // ESPERO UM STATUS OK e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(log.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("descricao").value(log.getDescricao()))
                .andExpect(MockMvcResultMatchers.jsonPath("evento").value(log.getEvento()))
                .andExpect(MockMvcResultMatchers.jsonPath("usuario.id").value(log.getUsuario().getId()));

    }
    @Test
    public void deveLancarUmBadRequestAoTentarCriarUmLogSemUsuario() throws Exception {

        LogDTO logDTO = Criar.logDTO();
        logDTO.setUsuario(id);

        Mockito.when(logService.criar(Mockito.any(Log.class))).thenThrow(ErroLogException.class);
        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.of(usuario));

        String json = new ObjectMapper().writeValueAsString(logDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) ;

    }


    @Test
    public void deveBuscarUmLogPorId() throws Exception {

        LogDTO logDTO = Criar.logDTO();
        logDTO.setUsuario(id);

        Mockito.when(logService.encontrarPorId(id)).thenReturn(Optional.of(log));

        String json = new ObjectMapper().writeValueAsString(logDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(log.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("descricao").value(log.getDescricao()))
                .andExpect(MockMvcResultMatchers.jsonPath("evento").value(log.getEvento()))
                .andExpect(MockMvcResultMatchers.jsonPath("usuario").value(log.getUsuario().getId()));

    }
    @Test
    public void deveLancarErroAoBuscarUmLogPorIdInValido() throws Exception {

        LogDTO logDTO = Criar.logDTO();
        logDTO.setUsuario(id);

        Mockito.when(logService.encontrarPorId(id)).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(logDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void deveDeletarUmLogPorIdInvalid() throws Exception {

        LogDTO logDTO = Criar.logDTO();
        logDTO.setUsuario(id);

        Mockito.when(logService.encontrarPorId(id)).thenReturn(Optional.of(log));
        Mockito.doNothing().when(logService).deletar(log);

        String json = new ObjectMapper().writeValueAsString(logDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void deveLancarUmaExcessaoAoTentarDeletarUmLogPorIdInvalid() throws Exception {

        LogDTO logDTO = Criar.logDTO();
        logDTO.setUsuario(id);

        Mockito.when(logService.encontrarPorId(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(logService).deletar(log);

        String json = new ObjectMapper().writeValueAsString(logDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
