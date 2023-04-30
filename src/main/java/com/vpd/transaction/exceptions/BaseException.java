package com.vpd.transaction.exceptions;


import com.vpd.transaction.dto.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class BaseException {
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errorMessage = fieldErrors.get(0).getDefaultMessage();
        return Response.generateResponse(
                errorMessage,
                FORBIDDEN,
                null
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<Object> handleBadRequest(BadRequest e) {
        return Response.generateResponse(
                e.getMessage(),
                BAD_REQUEST,
                e.getCause()
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException e) {
        return Response.generateResponse(
                e.getMessage(),
                BAD_REQUEST,
                e.getCause()
        );
    }

    @ExceptionHandler(value={InvalidUserCredentialException.class})
    public ResponseEntity<Object> handleBadCredentials(InvalidUserCredentialException e) {
        return Response.generateResponse(
                e.getMessage(),
                UNAUTHORIZED,
                e.getCause()
        );
    }

}
