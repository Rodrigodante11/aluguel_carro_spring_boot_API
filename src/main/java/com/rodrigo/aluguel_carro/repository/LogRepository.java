package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
