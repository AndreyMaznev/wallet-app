package com.manv.wallet_app.service;


import com.manv.wallet_app.exception.InvalidOperationException;
import com.manv.wallet_app.exception.NotSufficientFundsException;
import com.manv.wallet_app.exception.WalletNotFoundException;
import com.manv.wallet_app.model.Wallet;
import com.manv.wallet_app.operation.WalletOperationRequest;
import com.manv.wallet_app.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Optional<Wallet> getWalletByUUID(UUID uuid) {
        return walletRepository.findByUuid(uuid);
    }

    @Override
    @Transactional
    public ResponseEntity<Wallet> getBalance(UUID uuid) {
        Optional<Wallet> wallet = getWalletByUUID(uuid);
        if (wallet.isEmpty()) throw new WalletNotFoundException("Wallet not found");
        else {
            wallet.get().setBalance(wallet.get().getBalance().setScale(2, RoundingMode.HALF_UP));
            System.out.println();
            return new ResponseEntity<>(wallet.get(), HttpStatus.OK);
        }

//        if (wallet.isPresent()) return wallet.get().getBalance().setScale(2, BigDecimal.ROUND_HALF_UP);
//        else throw new WalletNotFoundException("Wallet not found");
    }


    //delete
    @Override
    public void createWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public void processOperation(WalletOperationRequest request) {
        Optional <Wallet> wallet = walletRepository.findById(request.getWalletUUID());
        if (wallet.isEmpty())
            throw new WalletNotFoundException("Wallet not found");
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidOperationException("Check spelling or value (should be positive number)");
        Wallet currentWallet = wallet.get();

        switch (request.getOperationType()) {
            case DEPOSIT:
                //Increasing current balance by the amount of the request
                currentWallet.setBalance(currentWallet.getBalance().add (request.getAmount()));
                break;
            case WITHDRAW:
                //-1 not enough money, 0 equals, +1 more than need
                if (currentWallet.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new NotSufficientFundsException("Not sufficient funds");
                }
                currentWallet.setBalance(currentWallet.getBalance().subtract(request.getAmount()));
                break;
            default:
                throw new InvalidOperationException("Invalid JSON format");
        }
        walletRepository.save(currentWallet);
    }
}

