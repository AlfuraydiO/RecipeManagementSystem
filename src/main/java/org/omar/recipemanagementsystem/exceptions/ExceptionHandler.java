/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipemanagementsystem.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author oalfuraydi
 */
@ControllerAdvice
public class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<ErrorMessage> handleEDAExceptions(ResponseStatusException exception) {
        ErrorMessage errorMessage = new ErrorMessage("EDA001", exception.getStatusCode().toString(), exception.getReason());
        return new ResponseEntity<>(errorMessage, exception.getStatusCode());

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleBadRequestExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorMessage errorMessage = new ErrorMessage("EDA002", exception.getStatusCode().toString(), errors.toString());
         
        return new ResponseEntity<>(errorMessage, exception.getStatusCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleIntegrity(DataIntegrityViolationException e) {
        return new ResponseEntity<>(
                new ErrorMessage("EDA003", "409", "Constraint violation"), HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataAccessException.class)   // parent of all Spring DB exceptions
    public ResponseEntity<ErrorMessage> handleDb(DataAccessException e) {
        log.error("Database Error: ",e);
        return new ResponseEntity<>(
                new ErrorMessage("EDA004", "500", "Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Unique index or primary key violation: "PUBLIC.CONSTRAINT_INDEX_8 ON PUBLIC.RATING(RECIPE_ID NULLS FIRST, USER_ACCOUNT_ID NULLS FIRST) VALUES ( /* key:1 */ CAST(1 AS BIGINT), 1)"
}
