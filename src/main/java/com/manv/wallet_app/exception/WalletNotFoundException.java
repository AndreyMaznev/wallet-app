package com.manv.wallet_app.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
