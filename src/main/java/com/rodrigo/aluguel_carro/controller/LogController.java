package com.rodrigo.aluguel_carro.controller;

import com.rodrigo.aluguel_carro.Utils.Converter;
import com.rodrigo.aluguel_carro.dto.LogDTO;
import com.rodrigo.aluguel_carro.entity.Log;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.exceptions.ErroLogException;
import com.rodrigo.aluguel_carro.service.LogService;
import com.rodrigo.aluguel_carro.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Api(value = "API REST Log System")
@CrossOrigin(origins = "*")
public class LogController {

    private final LogService logService;

    private final UsuarioService usuarioService;

    private void encontrarUsuario(Log log, LogDTO logDTO){
        Usuario usuario = usuarioService
                .obterPorId(logDTO.getUsuario())
                .orElseThrow( () -> new ErroClienteException("Cliente não encontrado Para registro de Log."));

        log.setUsuario(usuario);

    }

    @ApiOperation(value = "Criar Log")
    @PostMapping()
    public ResponseEntity<?> criarLog(@RequestBody LogDTO logDTO){
        try{

            Log log = Converter.log(logDTO);
            encontrarUsuario(log, logDTO);

            log = logService.criar(log);
            return new ResponseEntity<>(log, HttpStatus.CREATED);

        }catch(ErroLogException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @ApiOperation(value = "Deletar Log Por ID")
    @DeleteMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity<?> deletarLog(@PathVariable("id") Long id ){
        return logService.encontrarPorId(id).map( log -> {

            logService.deletar(log);
            return new ResponseEntity<>("Log  Deletado Com Sucesso",
                    HttpStatus.NO_CONTENT);

        }).orElseGet( () ->

                new ResponseEntity<>("Log não encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @ApiOperation(value = "Obeter Todos Logs")
    @GetMapping()
    public ResponseEntity<?> obterTodosLogs(){
        try{
            List<Log> logEntity =  logService.encontrarTodos();

            return new ResponseEntity<>(logEntity, HttpStatus.CREATED);

        }catch(ErroLogException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @ApiOperation(value = "Obter Log Por ID")
    @GetMapping("{id}")
    public ResponseEntity<?> obterLogPorId(@PathVariable("id") Long id){

        return logService.encontrarPorId(id)
                .map( log -> new ResponseEntity(
                                Converter.log(log), HttpStatus.OK
                        )
                ).orElseGet( () -> new ResponseEntity<>("Log Não encontrado", HttpStatus.NOT_FOUND ));
    }

}
