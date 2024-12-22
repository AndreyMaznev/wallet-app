package com.manv.wallet_app.exception;

import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity <ApplicationError> catchWalletNotFoundException (WalletNotFoundException e) {
        logger.error (e.getMessage(), e);
        return new ResponseEntity <> (
                new ApplicationError(HttpStatus.NOT_FOUND.value(),
                        "Wallet not found"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity <ApplicationError> catchInvalidOperationException (InvalidOperationException e) {
        logger.error (e.getMessage(), e);
        return new ResponseEntity <> (
                new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                        "Invalid operation"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotSufficientFundsException.class)
    public ResponseEntity <ApplicationError> catchNotSufficientFundsException (NotSufficientFundsException e) {
        logger.error (e.getMessage(), e);
        return new ResponseEntity <> (
                new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                        "Not Sufficient Funds"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <ApplicationError> catchIllegalArgumentException (MethodArgumentNotValidException e) {
        logger.error (e.getMessage(), e);
        return new ResponseEntity <> (
                new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                        "Illegal argument"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity <ApplicationError> catchHttpMessageNotReadableException (HttpMessageNotReadableException e) {
        logger.error (e.getMessage(), e);
        return new ResponseEntity <> (
                new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                        "Invalid JSON"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity <ApplicationError> catchAllInternalErrorException (Exception e) {
        logger.error (e.getMessage(), e);
        return new ResponseEntity <> (
                new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler (OptimisticLockException.class)
    public void catchOptimisticLockException (OptimisticLockException e) {
        logger.error (e.getMessage(), e);
        System.out.println("OPTIMISTIC LOCK EXCEPTION CAUGHT");
    }
}
