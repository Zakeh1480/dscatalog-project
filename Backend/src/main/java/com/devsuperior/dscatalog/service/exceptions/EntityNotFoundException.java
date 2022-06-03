package com.devsuperior.dscatalog.service.exceptions;

//Exception = tem que ser tratada, Runtime = é opcional
public class EntityNotFoundException extends RuntimeException{

    //Repassando o argumento para a superclasse
    public EntityNotFoundException(String msg){
        super(msg);
    }
}
