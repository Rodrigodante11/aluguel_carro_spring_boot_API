package com.rodrigo.aluguel_carro.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Api(value = "API REST Automovel")
@CrossOrigin(origins = "*")
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "API DE ALUGUEL DE CARRO DESENVOLVIDO POR Rodrigo Augusto de Oliveira";
    }
}
