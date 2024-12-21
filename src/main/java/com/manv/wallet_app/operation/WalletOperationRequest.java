package com.manv.wallet_app.operation;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletOperationRequest {
    @NotNull
    private UUID walletUUID;
    @NotNull
    private OperationType operationType;
    @NotNull
    private BigDecimal amount;

    public WalletOperationRequest(UUID walletUUID, OperationType operationType, BigDecimal amount) {
        this.walletUUID = walletUUID;
        this.operationType = operationType;
        this.amount = amount;
    }

    public UUID getWalletUUID() {
        return walletUUID;
    }

    public void setWalletUUID(UUID walletUUID) {
        this.walletUUID = walletUUID;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
