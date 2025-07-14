package br.com.acme.insurance.bdd.dto;

import java.util.List;

public class ErrorResponse {
    public int status;
    public String error;
    public List<FieldError> messages;
    public String timestamp;

    public static class FieldError {
        public String field;
        public String message;
    }
}