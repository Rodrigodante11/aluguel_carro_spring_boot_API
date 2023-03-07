package com.rodrigo.aluguel_carro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc // aceitar requisicao para consumir a API
@Configuration
public class WebConfiguration implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**") //url das API que podem ser consultada
                .allowedMethods("GET","POST","PUT","DELETE", "OPTIONS"); //
    }
}
