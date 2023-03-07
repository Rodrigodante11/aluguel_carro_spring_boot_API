package com.rodrigo.aluguel_carro.exceptions;

public class ErroUsuarioException extends RuntimeException{
    public ErroUsuarioException(String msg){
        super(msg);
    }
}
