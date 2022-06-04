package com.devsuperior.dscatalog.service.exceptions;

//Exception = tem que ser tratada, Runtime = é opcional
public class ResourceNotFoundException extends RuntimeException{

    //Repassando o argumento para a superclasse
    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
