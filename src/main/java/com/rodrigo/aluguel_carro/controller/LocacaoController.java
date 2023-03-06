package com.rodrigo.aluguel_carro.controller;

import com.rodrigo.aluguel_carro.Utils.Converter;
import com.rodrigo.aluguel_carro.dto.LocacaoDTO;
import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Cliente;
import com.rodrigo.aluguel_carro.entity.Locacao;
import com.rodrigo.aluguel_carro.exceptions.ErroAutomovelException;
import com.rodrigo.aluguel_carro.exceptions.ErroClienteException;
import com.rodrigo.aluguel_carro.exceptions.ErroLocacaoException;
import com.rodrigo.aluguel_carro.service.AutomovelService;
import com.rodrigo.aluguel_carro.service.ClienteService;
import com.rodrigo.aluguel_carro.service.LocacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locacao")
@RequiredArgsConstructor
public class LocacaoController {

    private final LocacaoService locacaoService;
    private final ClienteService clienteService;
    private final AutomovelService automovelService;

    @PostMapping()
    public ResponseEntity<?> salvarCliente(@RequestBody LocacaoDTO locacaoDTO){

        try{

            Cliente cliente = clienteService
                    .obterPorId(locacaoDTO.getCliente())
                    .orElseThrow( () -> new ErroClienteException("Cliente não encontrado."));

            Automovel automovel = automovelService
                    .obterPorId(locacaoDTO.getAutomovel())
                    .orElseThrow( () -> new ErroAutomovelException("Automovel não encontrado."));

            Locacao locacaoEntidade = Converter.locacao(locacaoDTO);
            locacaoEntidade.setAutomovel(automovel);
            locacaoEntidade.setCliente(cliente);

            locacaoEntidade = locacaoService.salvar(locacaoEntidade);
            return new ResponseEntity<>(locacaoEntidade, HttpStatus.CREATED);

        }catch(ErroClienteException | ErroAutomovelException | ErroLocacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterLocacaoPorId(@PathVariable("id") Long id){
        return locacaoService.obterPorId(id)
                .map( lancamneto -> new ResponseEntity(
                        Converter.locacao(lancamneto), HttpStatus.OK
                        )
                ).orElseGet( () -> new ResponseEntity<>("Locação Não encontrado", HttpStatus.NOT_FOUND ));
    }

    @DeleteMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity<?> deletarLocacao(@PathVariable("id") Long id ){
        return locacaoService.obterPorId(id).map( entidade -> {
            locacaoService.deletar(entidade);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseGet( () ->
                new ResponseEntity<>("Locação Não encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @GetMapping()
    public ResponseEntity<?> obterTodosALocacoess(){
        try{
            List<Locacao> locacoesEntidade =  locacaoService.obterTodos();

            return new ResponseEntity<>(locacoesEntidade, HttpStatus.CREATED);

        }catch(ErroClienteException | ErroAutomovelException | ErroLocacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizarLocacao( @PathVariable("id") Long id, @RequestBody LocacaoDTO locacaoDTO ) {
        return locacaoService.obterPorId(id).map( entity -> {
            try {

                Cliente cliente = clienteService
                        .obterPorId(locacaoDTO.getCliente())
                        .orElseThrow( () -> new ErroClienteException("Cliente não encontrado."));

                Automovel automovel = automovelService
                        .obterPorId(locacaoDTO.getAutomovel())
                        .orElseThrow( () -> new ErroAutomovelException("Automovel não encontrado."));

                Locacao locacaoEntidade = Converter.locacao(locacaoDTO);
                locacaoEntidade.setId(entity.getId());
                locacaoEntidade.setCliente(cliente);
                locacaoEntidade.setAutomovel(automovel);

                locacaoService.atualizar(locacaoEntidade);
                return ResponseEntity.ok(locacaoEntidade);

            }catch(ErroClienteException | ErroAutomovelException | ErroLocacaoException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity("Locação Não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

}
