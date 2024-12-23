package com.manv.wallet_app.service;

import com.manv.wallet_app.model.Wallet;
import com.manv.wallet_app.operation.WalletOperationRequest;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.UUID;

public interface WalletService {
    Optional<Wallet> getWalletByUUID(UUID uuid);
    ResponseEntity <?> processOperation (WalletOperationRequest request);
    ResponseEntity <Wallet> getBalance(UUID uuid);
}
