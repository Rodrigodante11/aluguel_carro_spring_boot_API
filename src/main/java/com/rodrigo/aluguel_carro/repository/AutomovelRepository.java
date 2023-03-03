package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.entity.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {

    List<Automovel> findAllByMarca(String marca);

    List<Automovel> findAllByModelo(String marca);

}
