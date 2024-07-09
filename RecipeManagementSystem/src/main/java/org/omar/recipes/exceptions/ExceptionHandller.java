/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipes.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author oalfuraydi
 */
@ControllerAdvice
public class ExceptionHandller {
    
    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<ErrorMessage> handleEDAExceptions(ResponseStatusException exception) {
        ErrorMessage errorMessage=new ErrorMessage(LocalDateTime.now().toString(), exception.getStatusCode(), exception.getReason());
        return new ResponseEntity<>(errorMessage, exception.getStatusCode());
    }
    
}
