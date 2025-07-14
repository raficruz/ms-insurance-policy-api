package br.com.acme.insurance.adapter.controller.exception.handler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    public ValidationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        List<Map<String, String>> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String message = messageSource.getMessage(fieldError, locale);
            errors.add(Map.of(
                    "field", fieldError.getField(),
                    "message", message
            ));
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("messages", errors);
        body.put("timestamp", new Date());
        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, Locale locale) {
        List<Map<String, String>> errors = new ArrayList<>();
        Throwable cause = ex.getCause();
        if (cause instanceof MismatchedInputException mismatched) {
            List<String> missingFields = new ArrayList<>();
            mismatched.getPath().forEach(ref -> missingFields.add(ref.getFieldName()));
            // Mensagem multilíngue
            String message = messageSource.getMessage("request.missing.fields", new Object[]{String.join(", ", missingFields)}, locale);
            errors.add(Map.of(
                    "field", String.join(", ", missingFields),
                    "message", message
            ));
        } else {
            String message = messageSource.getMessage("request.invalid.payload", null, locale);
            errors.add(Map.of(
                    "field", "",
                    "message", message
            ));
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("messages", errors);
        body.put("timestamp", new Date());
        return ResponseEntity.badRequest().body(body);
    }
}