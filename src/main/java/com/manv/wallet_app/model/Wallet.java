package com.manv.wallet_app.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "wallet")
public class Wallet {

    @Id
    @Column (name = "wallet_id", nullable = false)
    @NotNull
    private UUID uuid;

    @Column (name = "balance", nullable = false)
    @NotNull
    private BigDecimal balance;

    public Wallet() {
    }

    //Generating UUID in constructor with balance argument.
    public Wallet (BigDecimal balance) {
        this.uuid = generateUUID();
        this.balance = balance;
    }

    //UUID generation
    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public @NotNull BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(@NotNull BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "uuid=" + uuid +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(uuid, wallet.uuid) && Objects.equals(balance, wallet.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, balance);
    }
}

