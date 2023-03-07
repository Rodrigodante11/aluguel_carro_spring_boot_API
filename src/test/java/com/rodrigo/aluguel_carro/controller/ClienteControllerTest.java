package com.rodrigo.aluguel_carro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.dto.ClienteDTO;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.repository.ClienteRepository;
import com.rodrigo.aluguel_carro.service.ClienteService;
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
@ActiveProfiles("test")
@WebMvcTest(controllers = ClienteController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    static final String API= "/api/cliente";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    ClienteService clienteService;

    @MockBean
    ClienteRepository clienteRepository;


    @Test
    public void deveSalvarUmCliente() throws Exception {
        Cliente cliente = Criar.cliente();
        cliente.setId(1L);

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.salvar(Mockito.any(Cliente.class))).thenReturn(cliente);

        String json = new ObjectMapper().writeValueAsString(clienteDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isCreated()) // ESPERO UM STATUS OK e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cliente.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(cliente.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("idade").value(cliente.getIdade()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(cliente.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(cliente.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoRua").value(cliente.getEnderecoRua()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoNumero").value(cliente.getEnderecoNumero()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoComplemento").value(cliente
                        .getEnderecoComplemento()))
                .andExpect(MockMvcResultMatchers.jsonPath("cidade").value(cliente.getCidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("estado").value(cliente.getEstado()));

    }
    @Test
    public void deveLancarUmBadRequestAoTentarSalvarUmCliente() throws Exception {

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.salvar(Mockito.any(Cliente.class))).thenThrow(ErroClienteException.class);

        String json = new ObjectMapper().writeValueAsString(clienteDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveAtualizarUmCliente() throws Exception {
        Cliente cliente = Criar.cliente();
        Long id = 1L;
        cliente.setId(id);

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.salvar(Mockito.any(Cliente.class))).thenReturn(cliente);
        Mockito.when(clienteService.obterPorId(id)).thenReturn(Optional.of(cliente));

        String json = new ObjectMapper().writeValueAsString(clienteDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .put(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cliente.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(cliente.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("idade").value(cliente.getIdade()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(cliente.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(cliente.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoRua").value(cliente.getEnderecoRua()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoNumero").value(cliente.getEnderecoNumero()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoComplemento").value(cliente
                        .getEnderecoComplemento()))
                .andExpect(MockMvcResultMatchers.jsonPath("cidade").value(cliente.getCidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("estado").value(cliente.getEstado()));

    }

    @Test
    public void deveLancarErroAoAtualizarUmCliente() throws Exception {
        Cliente cliente = Criar.cliente();
        Long id = 1L;
        cliente.setId(id);

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.salvar(Mockito.any(Cliente.class))).thenReturn(cliente);
        Mockito.when(clienteService.obterPorId(id)).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(clienteDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .put(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
    @Test
    public void deveBuscarUmClientePorId() throws Exception {
        Cliente cliente = Criar.cliente();
        Long id = 1L;
        cliente.setId(id);

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.obterPorId(id)).thenReturn(Optional.of(cliente));

        String json = new ObjectMapper().writeValueAsString(clienteDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cliente.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(cliente.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("idade").value(cliente.getIdade()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(cliente.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(cliente.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoRua").value(cliente.getEnderecoRua()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoNumero").value(cliente.getEnderecoNumero()))
                .andExpect(MockMvcResultMatchers.jsonPath("enderecoComplemento").value(cliente
                        .getEnderecoComplemento()))
                .andExpect(MockMvcResultMatchers.jsonPath("cidade").value(cliente.getCidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("estado").value(cliente.getEstado()));

    }

    @Test
    public void deveLancarErroAoBuscarUmClientePorId() throws Exception {

        Long id = 10000L;

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.obterPorId(id)).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(clienteDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .get(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void deveLancarUmaExcessaoAoTentarDeletarUmClientePorId() throws Exception {

        Cliente cliente = Criar.cliente();
        Long id = 1L;
        cliente.setId(id);

        ClienteDTO clienteDTO = Criar.clienteDTO();

        Mockito.when(clienteService.obterPorId(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(clienteService).deletar(cliente);

        String json = new ObjectMapper().writeValueAsString(clienteDTO);

        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .delete(API.concat("/"+id))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
