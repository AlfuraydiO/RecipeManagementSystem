package org.omar.recipemanagementsystem.exceptions;

 import org.springframework.http.HttpStatusCode;
 import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {
    public ResourceNotFoundException(Long id) {
        super(HttpStatusCode.valueOf(404),"Entity not found id"+id.toString());
    }
}
