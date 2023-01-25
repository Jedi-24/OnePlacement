package com.Jedi.OnePlacementServer.exceptions;

import com.Jedi.OnePlacementServer.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // global exception handling for all controllers;
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        String message = exception.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);

        return new ResponseEntity<ApiResponse> (apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException exception){
        HashMap<String, String> ret = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String message = error.getDefaultMessage();
            String fieldName = ((FieldError)error).getField().toString();

            ret.put(fieldName,message);
        });

        return new ResponseEntity<>(ret,HttpStatus.BAD_REQUEST);
    }
}