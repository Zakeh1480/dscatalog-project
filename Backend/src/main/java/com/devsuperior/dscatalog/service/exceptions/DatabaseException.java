package com.devsuperior.dscatalog.service.exceptions;

//Exception = tem que ser tratada, Runtime = é opcional
public class DatabaseException extends RuntimeException{

    //Repassando o argumento para a superclasse
    public DatabaseException(String msg){
        super(msg);
    }
}
