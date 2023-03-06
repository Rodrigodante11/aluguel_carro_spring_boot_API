package com.rodrigo.aluguel_carro.controller;

import com.rodrigo.aluguel_carro.Utils.Converter;
import com.rodrigo.aluguel_carro.dto.AutomovelDTO;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.exceptions.ErroAutomovelException;
import com.rodrigo.aluguel_carro.service.AutomovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/automovel")
@RequiredArgsConstructor
public class AutomovelController {

    private final AutomovelService automovelService;

    @PostMapping()
    public ResponseEntity<?> salvarAutomovel(@RequestBody AutomovelDTO automovelDTO){
        try{
            Automovel automovelEntidade = Converter.automovel(automovelDTO);
            automovelEntidade = automovelService.salvar(automovelEntidade);
            return new ResponseEntity<>(automovelEntidade, HttpStatus.CREATED);

        }catch(ErroAutomovelException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterAutomovelPorId(@PathVariable("id") Long id){
        return automovelService.obterPorId(id)
                .map( automovel -> new ResponseEntity(
                                Converter.automovel(automovel), HttpStatus.OK
                        )
                ).orElseGet( () -> new ResponseEntity<>("Automovel Não encontrado", HttpStatus.NOT_FOUND ));
    }

    @GetMapping()
    public ResponseEntity<?> obterTodosAutomoveis(){
        try{
            List<Automovel> automovelEntidade =  automovelService.obterTodos();

            return new ResponseEntity<>(automovelEntidade, HttpStatus.CREATED);

        }catch(ErroAutomovelException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizarAutomovel( @PathVariable("id") Long id, @RequestBody AutomovelDTO automovelDTO ) {
        return automovelService.obterPorId(id).map( entity -> {

            try {
                Automovel automovel = Converter.automovel(automovelDTO);
                automovel.setId(entity.getId());
                automovelService.atualizar(automovel);
                return ResponseEntity.ok(automovel);

            }catch (ErroAutomovelException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity<>("Automovel não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @DeleteMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity<?> deletarAutomovel(@PathVariable("id") Long id ){
        return automovelService.obterPorId(id).map( automovel -> {
            automovelService.deletar(automovel);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseGet( () ->
                new ResponseEntity<>("Automovel não encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @PostMapping("/marca")
    public ResponseEntity<?> obterPorMarca(@RequestBody(required = false) Map marca){
        try {

            List<Automovel> listAutomovelEncontrado = automovelService.obterPorMarca(marca.get("marca").toString());

            if (listAutomovelEncontrado.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        "Não foi possivel realizar a consulta, Automovel não encontrado pela Marca informada");
            }

            List<AutomovelDTO> listDTOAutomovelEncontrado = Converter.automovel(listAutomovelEncontrado);

            return ResponseEntity.ok(listDTOAutomovelEncontrado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/modelo")
    public ResponseEntity<?> obterPorModelo(@RequestBody(required = false) Map modelo){
        try {

            List<Automovel> listAutomovelEncontrado = automovelService.obterPorModelo(modelo.get("modelo").toString());

            if (listAutomovelEncontrado.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        "Nao foi possivel realizar a consulta, Automovel não encontrado pelo Modelo informada");
            }

            List<AutomovelDTO> listDTOAutomovelEncontrado = Converter.automovel(listAutomovelEncontrado);

            return ResponseEntity.ok(listDTOAutomovelEncontrado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
