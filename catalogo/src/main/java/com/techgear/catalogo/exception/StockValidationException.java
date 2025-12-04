package com.techgear.catalogo.exception;

public class StockValidationException extends RuntimeException {
    
    public StockValidationException(String message) {
        super(message);
    }
    
    public StockValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
