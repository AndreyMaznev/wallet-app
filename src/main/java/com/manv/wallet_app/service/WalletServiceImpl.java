package com.manv.wallet_app.service;


import com.manv.wallet_app.exception.InvalidOperationException;
import com.manv.wallet_app.exception.NotSufficientFundsException;
import com.manv.wallet_app.exception.WalletNotFoundException;
import com.manv.wallet_app.model.Wallet;
import com.manv.wallet_app.operation.WalletOperationRequest;
import com.manv.wallet_app.repository.WalletRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
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
    }

    @Override
    public ResponseEntity<?> createWallet(Wallet wallet) {
        if (wallet == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (walletRepository.findByUuid(wallet.getUuid()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        walletRepository.save(wallet);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> deleteWallet(UUID uuid) {
        Optional<Wallet> wallet = getWalletByUUID(uuid);
        if (wallet.isEmpty()) throw new WalletNotFoundException("Wallet not found");
        walletRepository.deleteById(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity <Wallet> processOperation(WalletOperationRequest request) {
        int retries = 0;
        while (retries < 5) {
            retries++;
            try {
                Optional<Wallet> wallet = walletRepository.findById(request.getWalletUUID());
                if (wallet.isEmpty())
                    throw new WalletNotFoundException("Wallet not found");
                if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) < 0)
                    throw new InvalidOperationException("Check spelling or value (should be positive number)");
                Wallet currentWallet = wallet.get();
                switch (request.getOperationType()) {
                    case DEPOSIT:
                        //Increasing current balance by the amount of the request
                        currentWallet.setBalance(currentWallet.getBalance().add(request.getAmount()));
                        break;
                    case WITHDRAW:
                        //-1 not enough money, 0 equals, +1 enough money
                        if (currentWallet.getBalance().compareTo(request.getAmount()) < 0) {
                            throw new NotSufficientFundsException("Not sufficient funds");
                        }
                        currentWallet.setBalance(currentWallet.getBalance().subtract(request.getAmount()));
                        break;
                    default:
                        throw new InvalidOperationException("Invalid operation type");
                }
                walletRepository.save(currentWallet);
                walletRepository.flush();
                return new ResponseEntity<>(currentWallet, HttpStatus.OK);
            } catch (OptimisticLockException e) {
                System.out.println("Caught an exception" + e.getMessage() + "-----" + e.getClass());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new ConcurrencyFailureException("Interrupted while waiting for lock");
                }
            }
        }
        throw new RuntimeException("Something went wrong");
    }
}
