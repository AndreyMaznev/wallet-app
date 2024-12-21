package com.manv.wallet_app.repository;


import com.manv.wallet_app.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository <Wallet, UUID> {
    Optional <Wallet> findByUuid (UUID uuid);

}
