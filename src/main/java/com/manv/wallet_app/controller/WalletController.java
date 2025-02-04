package com.manv.wallet_app.controller;

import com.manv.wallet_app.model.Wallet;
import com.manv.wallet_app.operation.WalletOperationRequest;
import com.manv.wallet_app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(value = "/wallets/{WALLET_UUID}")
    public ResponseEntity<?> getWalletBalance(@PathVariable (name = "WALLET_UUID") UUID uuid) {
        return walletService.getBalance(uuid);
    }

    @PostMapping  (value = "/wallet")
    public ResponseEntity <?> processWalletOperation(@RequestBody WalletOperationRequest walletOperationRequest) {
        return walletService.processOperation(walletOperationRequest);
    }

    @PostMapping  (value = "/create")
    public ResponseEntity <?> createWallet(@RequestBody Wallet wallet) {
        Wallet tempWallet = new Wallet(wallet.getBalance());
        System.out.println("New wallet UUID " + tempWallet.getUuid());
        walletService.createWallet(tempWallet);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @GetMapping  (value = "/delete/{WALLET_UUID}")
    public ResponseEntity <?> deleteWallet(@PathVariable (name = "WALLET_UUID") UUID uuid) {
        System.out.println("Deleting wallet with UUID " + uuid);
        walletService.deleteWallet(uuid);
        return new ResponseEntity<> (HttpStatus.OK);
    }
}
