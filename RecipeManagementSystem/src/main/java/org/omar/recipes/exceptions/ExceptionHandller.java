/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipes.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author oalfuraydi
 */
@ControllerAdvice
public class ExceptionHandller {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<ErrorMessage> handleEDAExceptions(ResponseStatusException exception) {
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now().toString(), exception.getStatusCode(), exception.getReason());
        return new ResponseEntity<>(errorMessage, exception.getStatusCode());

    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleBadRequestExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now().toString(), exception.getStatusCode(), errors.toString().replace("{}", ""));
         
        return new ResponseEntity<>(errorMessage, exception.getStatusCode());
    }

    //org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Unique index or primary key violation: "PUBLIC.CONSTRAINT_INDEX_8 ON PUBLIC.RATING(RECIPE_ID NULLS FIRST, USER_ACCOUNT_ID NULLS FIRST) VALUES ( /* key:1 */ CAST(1 AS BIGINT), 1)"
}
