package com.rodrigo.aluguel_carro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        PasswordEncoder encoder= new BCryptPasswordEncoder(); // algoritimo de autenticacao para criptoghrafica sempre gera um hash diferente
//        // diferente do MD5 que sempre gera o mesmo Hash
//
//        return encoder;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) { // usar usuario e senha em memoria e nao aquele hash que aparece em memoria
        String senhaCodificada = passwordEncoder.encode("qwe123Rodrigo");
        String senhaCodificadaAdimin = passwordEncoder.encode("qwe123Admin");

        UserDetails user = User.withUsername("userRodrigo")
                .password(senhaCodificada)
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("adminRodrigo")
                .password(senhaCodificadaAdimin)
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // pedir autorizacao para acessar API
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}