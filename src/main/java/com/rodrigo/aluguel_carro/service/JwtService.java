package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {

    String gerarToken(Usuario usuario);

    // Claims seu as informacoes que tem no tokem
    Claims obterClaims(String token) throws ExpiredJwtException;

    boolean isTokenValido(String token);

    String obterLoginUsuario(String token);

}