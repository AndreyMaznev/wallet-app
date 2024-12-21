package com.manv.wallet_app.exception;

public class NotSufficientFundsException extends RuntimeException {
    public NotSufficientFundsException(String errorMessage) {
        super(errorMessage);
    }
}
