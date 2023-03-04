package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.entity.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
}
