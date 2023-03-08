package com.rodrigo.aluguel_carro.controller;

import com.rodrigo.aluguel_carro.Utils.Converter;
import com.rodrigo.aluguel_carro.dto.TokenDTO;
import com.rodrigo.aluguel_carro.dto.UsuarioDTO;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.exceptions.ErroUsuarioException;
import com.rodrigo.aluguel_carro.service.JwtService;
import com.rodrigo.aluguel_carro.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@Api(value = "API REST Usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final JwtService jwtService;

    @ApiOperation(value = "Salvar Usuario")
    @PostMapping()
    public ResponseEntity<?> salvarUsuario(@RequestBody UsuarioDTO usuarioDTO){

        try{
            Usuario usuario = Converter.usuario(usuarioDTO);

            usuario = usuarioService.salvar(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);

        }catch (ErroUsuarioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Deletar Usuario por Id")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable("id") Long id ){
        return usuarioService.obterPorId(id).map( usuario -> {

            usuarioService.deletar(usuario);
            return new ResponseEntity<>("Usuario "+ usuario.getNome() +" Deletado Com Sucesso",
                    HttpStatus.NO_CONTENT);

        }).orElseGet( () ->
                new ResponseEntity<>("Usuario nao encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @ApiOperation(value = "Autenticar Usuario por email e senha")
    @PostMapping("/autenticar")
    public ResponseEntity autenticarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        try{
            Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());

            // return new ResponseEntity(body, status);
            String token = jwtService.gerarToken(usuarioAutenticado);
            TokenDTO tokenDTO = new TokenDTO( usuarioAutenticado.getNome(), token);

            return ResponseEntity.ok(tokenDTO);

        }catch(ErroUsuarioException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}
