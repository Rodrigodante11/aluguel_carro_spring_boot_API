package com.rodrigo.aluguel_carro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.dto.UsuarioDTO;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.exceptions.ErroUsuarioException;
import com.rodrigo.aluguel_carro.service.UsuarioService;
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
@WebMvcTest(controllers = UsuarioController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    static final String API= "/api/usuario";

    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService usuarioService;


    @Test
    public void deveCriarUmUsuario() throws Exception {

        Long id =  1L;
        UsuarioDTO usuarioDTO = Criar.usuarioDTO();
        Usuario usuario = Criar.usuario();
        usuario.setId(id);

        Mockito.when(usuarioService.salvar(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isCreated()) // ESPERO UM STATUS OK e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("senha").value(usuario.getSenha()));

    }

    @Test
    public void deveancarUmBadRequestAoTentarCriarUmUsuario() throws Exception {

        Mockito.when(usuarioService.salvar(Mockito.any(Usuario.class))).thenThrow(ErroUsuarioException.class);
        UsuarioDTO usuarioDTO = Criar.usuarioDTO();

        String json = new ObjectMapper().writeValueAsString(usuarioDTO); // cast para Json

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveDeletarUmUsuarioPorId() throws Exception {

        Long id =  1L;
        UsuarioDTO usuarioDTO = Criar.usuarioDTO();
        Usuario usuario = Criar.usuario();
        usuario.setId(id);

        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.of(usuario));
        Mockito.doNothing().when(usuarioService).deletar(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void deveLancarUmaExcessaoAoTentarDeletarUmUsuarioPorIdInvalid() throws Exception {

        Long id =  1L;
        UsuarioDTO usuarioDTO = Criar.usuarioDTO();
        Usuario usuario = Criar.usuario();
        usuario.setId(id);

        Mockito.when(usuarioService.obterPorId(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(usuarioService).deletar(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveAutenticarUmUsuario() throws Exception {

        Long id =  1L;
        UsuarioDTO usuarioDTO = Criar.usuarioDTO();
        Usuario usuario = Criar.usuario();
        usuario.setId(id);

        Mockito.when(usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha())).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .contentType(JSON)
                .accept(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("senha").value(usuario.getSenha()));

    }

}
