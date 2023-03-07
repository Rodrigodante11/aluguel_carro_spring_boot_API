package com.rodrigo.aluguel_carro.controller;

import com.rodrigo.aluguel_carro.Utils.Converter;
import com.rodrigo.aluguel_carro.dto.ClienteDTO;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.service.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@Api(value = "API REST Cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @ApiOperation(value = "Salvar Cliente")
    @PostMapping()
    public ResponseEntity<?> salvarCliente(@RequestBody ClienteDTO clienteDTO){
        try{

            Cliente clienteEntidade = Converter.cliente(clienteDTO);
            clienteEntidade = clienteService.salvar(clienteEntidade);

            return new ResponseEntity<>(clienteEntidade, HttpStatus.CREATED);

        }catch(ErroClienteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Buscar Cliente por Id")
    @GetMapping("{id}")
    public ResponseEntity<?> obterClientePorId(@PathVariable("id") Long id){
        return clienteService.obterPorId(id)
                .map( cliente -> new ResponseEntity(
                                Converter.cliente(cliente), HttpStatus.OK
                        )
                ).orElseGet( () -> new ResponseEntity<>("Cliente Não encontrado", HttpStatus.NOT_FOUND ));
    }

    @ApiOperation(value = "Buscar todos Clientes")
    @GetMapping()
    public ResponseEntity<?> obterTodosAClientes(){
        try{
            List<Cliente> clienteEntidade =  clienteService.obterTodos();

            return new ResponseEntity<>(clienteEntidade, HttpStatus.CREATED);

        }catch(ErroClienteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Atualizar Cliente por Id")
    @PutMapping("{id}")
    public ResponseEntity<?> atualizarCliente( @PathVariable("id") Long id, @RequestBody ClienteDTO clienteDTO ) {
        return clienteService.obterPorId(id).map( entity -> {

            try {
                Cliente cliente = Converter.cliente(clienteDTO);
                cliente.setId(entity.getId());
                clienteService.atualizar(cliente);
                return ResponseEntity.ok(cliente);

            }catch (ErroClienteException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity<>("Cliente não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @ApiOperation(value = "Deletar Cliente por Id")
    @DeleteMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity<?> deletarCliente(@PathVariable("id") Long id ){
        return clienteService.obterPorId(id).map( cliente -> {

            clienteService.deletar(cliente);
            return new ResponseEntity<>("Cliente "+ cliente.getNome() +" Deletado Com Sucesso",
                    HttpStatus.NO_CONTENT);

        }).orElseGet( () ->
                new ResponseEntity<>("Cliente nao encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @ApiOperation(value = "Buscar Cliente por nome")
    @PostMapping("/nome")
    public ResponseEntity<?> obterPorNome(@RequestBody(required = false) Map nome){
        try {

            List<Cliente> listClienteEncontrado = clienteService.buscarClientesPorNome(nome.get("nome").toString());

            if (listClienteEncontrado.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        "Não foi possivel realizar a consulta, Cliente nao encontrado pelo nome informada");
            }

            List<ClienteDTO> listDTOClienteEncontrado = Converter.cliente(listClienteEncontrado);

            return ResponseEntity.ok(listDTOClienteEncontrado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
