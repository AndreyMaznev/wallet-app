package com.manv.wallet_app.exception;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException(String errorMessage) {
        super(errorMessage);
    }
}
