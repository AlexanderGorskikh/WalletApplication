package ru.example.walletapplication.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String msg){
        super(msg);
    }
}
