package com.rodrigo.aluguel_carro.exceptions;

public class ErroClienteException extends RuntimeException{
    public ErroClienteException(String msg){
        super(msg);
    }
}
