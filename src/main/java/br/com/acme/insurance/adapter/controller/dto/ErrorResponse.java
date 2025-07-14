package br.com.acme.insurance.adapter.controller.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String error;
    private List<String> messages;
    private OffsetDateTime timestamp;
}