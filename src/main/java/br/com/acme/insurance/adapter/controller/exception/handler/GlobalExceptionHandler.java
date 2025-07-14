package br.com.acme.insurance.adapter.controller.exception.handler;

import br.com.acme.insurance.adapter.controller.dto.ErrorResponse;
import br.com.acme.insurance.adapter.controller.exception.InsuranceAlreadyCanceledException;
import br.com.acme.insurance.adapter.controller.exception.InsuranceByCustomerNotFoundException;
import br.com.acme.insurance.adapter.controller.exception.InsuranceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InsuranceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(InsuranceNotFoundException ex) {
        String msg = messageSource.getMessage(
                "insurance.not.found",
                new Object[]{ex.getId()},
                Locale.getDefault()
        );
        ErrorResponse error = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .messages(List.of(msg))
                .timestamp(java.time.OffsetDateTime.now())
                .build();
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(InsuranceByCustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleByCustomerNotFound(InsuranceByCustomerNotFoundException ex) {
        String msg = messageSource.getMessage(
                "insurance.customer.not.found",
                new Object[]{ex.getCustomerId()},
                Locale.getDefault()
        );
        ErrorResponse error = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .messages(List.of(msg))
                .timestamp(java.time.OffsetDateTime.now())
                .build();
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(InsuranceAlreadyCanceledException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyCanceled(InsuranceAlreadyCanceledException ex) {
        String msg = messageSource.getMessage(
                "insurance.already.canceled",
                new Object[]{ex.getId()},
                Locale.getDefault()
        );
        ErrorResponse error = ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .messages(List.of(msg))
                .timestamp(java.time.OffsetDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(error);
    }
}
