package ru.example.walletapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    private String message;
    private String debugMessage;

    public ExceptionResponse(String message, String debugMessage) {
        this.message = message;
        this.debugMessage = debugMessage;
    }
}
