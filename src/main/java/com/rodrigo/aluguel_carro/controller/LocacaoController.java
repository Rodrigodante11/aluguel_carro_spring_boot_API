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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locacao")
@RequiredArgsConstructor
@Api(value = "API REST Locacao")
@CrossOrigin(origins = "*")
public class LocacaoController {

    private final LocacaoService locacaoService;
    private final ClienteService clienteService;
    private final AutomovelService automovelService;

    private void encontrarClienteEAutomovel(Locacao locacao, LocacaoDTO locacaoDTO){
        Cliente cliente = clienteService
                .obterPorId(locacaoDTO.getCliente())
                .orElseThrow( () -> new ErroClienteException("Cliente não encontrado."));

        Automovel automovel = automovelService
                .obterPorId(locacaoDTO.getAutomovel())
                .orElseThrow( () -> new ErroAutomovelException("Automovel não encontrado."));

        locacao.setAutomovel(automovel);
        locacao.setCliente(cliente);
    }

    @ApiOperation(value = "Salvar Locação")
    @PostMapping()
    public ResponseEntity<?> salvarLocacao(@RequestBody LocacaoDTO locacaoDTO){

        try{

            Locacao locacao = Converter.locacao(locacaoDTO);
            encontrarClienteEAutomovel(locacao, locacaoDTO);

            locacao = locacaoService.salvar(locacao);
            return new ResponseEntity<>(locacao, HttpStatus.CREATED);

        }catch(ErroClienteException | ErroAutomovelException | ErroLocacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @ApiOperation(value = "Obter Locação por ID")
    @GetMapping("{id}")
    public ResponseEntity<?> obterLocacaoPorId(@PathVariable("id") Long id){
        return locacaoService.obterPorId(id)
                .map( lancamneto -> new ResponseEntity(
                        Converter.locacao(lancamneto), HttpStatus.OK
                        )
                ).orElseGet( () -> new ResponseEntity<>("Locação Não encontrado", HttpStatus.NOT_FOUND ));
    }

    @ApiOperation(value = "Deletar Locação por ID")
    @DeleteMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity<?> deletarLocacao(@PathVariable("id") Long id ){
        return locacaoService.obterPorId(id).map( entidade -> {
            locacaoService.deletar(entidade);
            return new ResponseEntity<>("Locação Deletado Com Sucesso",
                    HttpStatus.NO_CONTENT);
        }).orElseGet( () ->
                new ResponseEntity<>("Locação Não encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @ApiOperation(value = "Obter todas locações")
    @GetMapping()
    public ResponseEntity<?> obterTodosALocacoess(){
        try{
            List<Locacao> locacoesEntidade =  locacaoService.obterTodos();

            return new ResponseEntity<>(locacoesEntidade, HttpStatus.CREATED);

        }catch(ErroClienteException | ErroAutomovelException | ErroLocacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Atualiza Locação por ID")
    @PutMapping("{id}")
    public ResponseEntity<?> atualizarLocacao( @PathVariable("id") Long id, @RequestBody LocacaoDTO locacaoDTO ) {
        return locacaoService.obterPorId(id).map( entity -> {
            try {


                Locacao locacao = Converter.locacao(locacaoDTO);
                locacao.setId(entity.getId());

                encontrarClienteEAutomovel(locacao, locacaoDTO);

                locacaoService.atualizar(locacao);
                return ResponseEntity.ok(locacao);

            }catch(ErroClienteException | ErroAutomovelException | ErroLocacaoException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity<>("Locação Não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

}
