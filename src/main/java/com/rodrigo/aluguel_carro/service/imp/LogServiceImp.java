package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Log;
import com.rodrigo.aluguel_carro.repository.LogRepository;
import com.rodrigo.aluguel_carro.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LogServiceImp implements LogService {

    private LogRepository logRepository;

    public LogServiceImp(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public Log criar(Log log) {

        return logRepository.save(log);
    }

    @Override
    public void deletar(Log log) {
        Objects.requireNonNull(log.getId());
        logRepository.delete(log);
    }

    @Override
    public List<Log> encontrarTodos() {
        return logRepository.findAll();
    }

    @Override
    public Optional<Log> encontrarPorId(Long id) {
        return logRepository.findById(id);
    }
}
