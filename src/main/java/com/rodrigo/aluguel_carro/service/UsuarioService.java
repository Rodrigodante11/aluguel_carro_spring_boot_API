package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario salvar(Usuario usuario);

    void deletar(Usuario usuario);

    void validar(Usuario usuario);

    void validarEmail(Usuario usuario);

    Optional<Usuario> obterPorId(Long id);

    Usuario autenticar(String email, String senha);
}
