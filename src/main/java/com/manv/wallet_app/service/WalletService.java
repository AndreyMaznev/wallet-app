package com.manv.wallet_app.service;

import com.manv.wallet_app.model.Wallet;
import com.manv.wallet_app.operation.WalletOperationRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletService {
    Optional<Wallet> getWalletByUUID(UUID uuid);
    void processOperation (WalletOperationRequest request);
    ResponseEntity <Wallet> getBalance(UUID uuid);
    void createWallet(Wallet wallet);
}
