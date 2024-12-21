package com.manv.wallet_app.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String errorMessage) {
        super (errorMessage);
    }
}
