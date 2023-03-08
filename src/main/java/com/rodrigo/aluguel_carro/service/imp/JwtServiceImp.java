package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.Instant;

@Service
public class JwtServiceImp implements JwtService {

    @Value("${jwt.expiracao}") // esta em application.properties
    private String expiracao;

    @Value("${jwt.chave-assinatura}") // esta em application.properties
    private String chaveAssinatura;

    @Override
    public String gerarToken(Usuario usuario) {

        long exp = Long.valueOf(expiracao);

        // pegar a Hora Atual e adiciona mais o tempo de expiracao (30 minutos)
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);

        // pega a hora local do computador pegando a zona/fusohorario do SO local
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();

        //java.util.Date;
        Date data = Date.from(instant);

        String horaExpiracaoToken = dataHoraExpiracao.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        String token = Jwts
                .builder()
                .setExpiration(data)
                .setSubject(usuario.getEmail()) // algo unico do usuario no caso email
                .claim("userid", usuario.getId())
                .claim("nome", usuario.getNome())     // clain sao informacoes a mais que estaram no token
                .claim("horaExpiracao", horaExpiracaoToken)
                .signWith( SignatureAlgorithm.HS512, chaveAssinatura) // assinar o tokem com esse algoritimo
                .compact();

        return token;

    }

    @Override
    public Claims obterClaims(String token) throws ExpiredJwtException { // claims sao as informacoes dentro dos tokens (nome , email ...)

        return Jwts
                .parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();

    }

    @Override
    public boolean isTokenValido(String token) {

        try {
            Claims claims = obterClaims(token);
            java.util.Date dataEx = claims.getExpiration();
            LocalDateTime dataExpiracao = dataEx.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            boolean dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
            return !dataHoraAtualIsAfterDataExpiracao;
        }catch(ExpiredJwtException e) {
            return false;
        }
    }

    @Override
    public String obterLoginUsuario(String token) {
        Claims claims = obterClaims(token);
        return claims.getSubject();   // pois foi setado no subject (.setSubject(usuario.getEmail()))  em (gerarToken)
    }
}
