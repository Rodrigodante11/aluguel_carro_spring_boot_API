package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Automovel;
import com.rodrigo.aluguel_carro.entity.Locacao;

import java.util.List;

public interface LocacaoService {

    Locacao salvar(Locacao locacao);

    Locacao atualizar(Locacao locacao);

    void deletar(Locacao locacao);

    List<Locacao> buscar(Locacao locacao);

    void validar(Locacao locacao);
}
