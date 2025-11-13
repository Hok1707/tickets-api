package com.kimhok.tickets.exception;

public class QrGenerationException extends RuntimeException {
    public QrGenerationException(String message,Object o) {
        super(message);
    }
}
