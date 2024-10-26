package it.librone.okipo.task.Exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

@ControllerAdvice
public class Handler {

    @Autowired
    private Logger log;

    @ExceptionHandler(MethodArgumentNotValidException .class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            if(fieldName.equals("Address")){
                errorMessage = "Invalid address";
                errors.put("Error", errorMessage);
            }else{
                errors.put(fieldName, errorMessage);
            }

        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ApiKeyNotValidException .class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> apiKeyNotValid(ApiKeyNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Error", ex.getMessage());
        log.error("Error from EthScan: "+ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericEthScanException .class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> genericErrorEthScan(GenericEthScanException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Error", ex.getMessage());
        log.error("Error from EthScan: "+ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
