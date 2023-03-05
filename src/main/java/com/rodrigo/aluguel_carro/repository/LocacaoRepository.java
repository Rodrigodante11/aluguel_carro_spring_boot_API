package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.entity.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    List<Locacao> findAllByCliente_Id(Long id);

    List<Locacao> findAllByAutomovel_Id(Long id);
}
