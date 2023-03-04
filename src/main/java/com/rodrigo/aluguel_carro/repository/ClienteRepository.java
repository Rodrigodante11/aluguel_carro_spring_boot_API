package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
