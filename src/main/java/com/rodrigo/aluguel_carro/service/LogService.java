package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Log;

import java.util.List;
import java.util.Optional;

public interface LogService {

    Log criar(Log log);
    void deletar(Log log);

    List<Log> encontrarTodos();

    Optional<Log> encontrarPorId(Long log);

}
