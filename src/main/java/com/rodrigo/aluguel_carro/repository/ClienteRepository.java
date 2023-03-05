package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByNomeContaining(String nome);

    boolean existsByEmail(String email);
}
