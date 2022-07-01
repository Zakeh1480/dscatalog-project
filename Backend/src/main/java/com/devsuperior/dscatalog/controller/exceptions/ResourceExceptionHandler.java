package com.devsuperior.dscatalog.controller.exceptions;

import com.devsuperior.dscatalog.service.exceptions.DatabaseException;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

//Interceptador dos error
@ControllerAdvice
public class ResourceExceptionHandler {

    //Informa ao projeto(spring) que esse método ira tratar essa exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException entityNotFoundException,
                                                        HttpServletRequest httpServletRequest) {
        StandardError standardError = new StandardError();

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.NOT_FOUND.value());
        standardError.setError("Controller not found");
        standardError.setMessage(entityNotFoundException.getMessage());
        standardError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(404).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseBadRequest(DatabaseException databaseException,
                                                            HttpServletRequest httpServletRequest) {
        StandardError standardError = new StandardError();

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setError("Database exception");
        standardError.setMessage(databaseException.getMessage());
        standardError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(400).body(standardError);
    }

    @ExceptionHandler(com.devsuperior.dscatalog.service.exceptions.MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> databaseBadRequest(MethodArgumentNotValidException methodArgumentNotValidException,
                                                              HttpServletRequest httpServletRequest) {
        ValidationError validationError = new ValidationError();

        validationError.setTimestamp(Instant.now());
        validationError.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        validationError.setError("Database exception");
        validationError.setMessage(methodArgumentNotValidException.getMessage());
        validationError.setPath(httpServletRequest.getRequestURI());

        //Passando uma lista com o nome do campo e a mensagem com erro
        //Usamos o FieldError do Spring para acessar os campos e o próprio erro para fazer o binding
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(422).body(validationError);
    }
}
