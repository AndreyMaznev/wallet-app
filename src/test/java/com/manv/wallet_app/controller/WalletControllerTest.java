package com.manv.wallet_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manv.wallet_app.exception.InternalErrorException;
import com.manv.wallet_app.exception.InvalidOperationException;
import com.manv.wallet_app.exception.NotSufficientFundsException;
import com.manv.wallet_app.exception.WalletNotFoundException;
import com.manv.wallet_app.model.Wallet;
import com.manv.wallet_app.operation.OperationType;
import com.manv.wallet_app.operation.WalletOperationRequest;
import com.manv.wallet_app.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper mapper;

    @MockitoBean
    private WalletService walletService;


    Wallet wallet = new Wallet(new BigDecimal(5000));

    //for exceptions
    WalletOperationRequest depositOperationRequest =
            new WalletOperationRequest(UUID.fromString("d9202867-79f7-4fc9-9fce-6722edcb6a5b")
                    , OperationType.WITHDRAW
                    , BigDecimal.valueOf(-100L));
    {
        //Always new UUID generated inside the class, need the static one.
        wallet.setUuid(UUID.fromString("d9202867-79f7-4fc9-9fce-6722edcb6a5a"));
    }

    @Test
    void getBalance_returnsRightBalanceThroughJsonWithRightStatus() throws Exception {
        Mockito.when(walletService.getBalance(wallet.getUuid()))
                .thenReturn(new ResponseEntity<>(wallet,HttpStatus.OK));
        mockMvc.perform(get("/api/v1/wallets/d9202867-79f7-4fc9-9fce-6722edcb6a5a")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(5000L));
    }

    @Test
    void getBalance_returnsWalletNotFoundThroughJsonWhenWrongWalletUuid() throws Exception {
        Mockito.when(walletService.getBalance(UUID.fromString ("d9202867-79f7-4fc9-9fce-6722edcb6a5b")))
                        .thenThrow(new WalletNotFoundException("Wallet not found"));

        mockMvc.perform(get("/api/v1/wallets/d9202867-79f7-4fc9-9fce-6722edcb6a5b")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Wallet not found"));
    }


    @Test
    void getInvalidOperationException_whenOperationIsWrong() throws Exception {
        Mockito.when(this.walletService.processOperation(Mockito.any(WalletOperationRequest.class)))
                        .thenThrow(new InvalidOperationException("Invalid operation"));

        mockMvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(depositOperationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8"))
                    .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid operation"));
    }

    @Test
    void getNotSufficientFundsException_whenBalanceIsLessThanOperationAmount() throws Exception {
        Mockito.when(this.walletService.processOperation(Mockito.any(WalletOperationRequest.class)))
                .thenThrow(new NotSufficientFundsException("Not Sufficient Funds"));

        mockMvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(depositOperationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWalletNotFoundException_whenWalletNumberIsWrong() throws Exception {
        Mockito.when(this.walletService.processOperation(Mockito.any(WalletOperationRequest.class)))
                .thenThrow(new WalletNotFoundException("Wallet not found"));

        mockMvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(depositOperationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getException_whenServerError() throws Exception {
        Mockito.when(this.walletService.processOperation(Mockito.any(WalletOperationRequest.class)))
                .thenThrow(new InternalErrorException("Internal server error"));

        mockMvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(depositOperationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getSuccessfulStatus_whenAllIsOk() throws Exception {
        Mockito.when(this.walletService.processOperation(Mockito.mock()))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(depositOperationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}