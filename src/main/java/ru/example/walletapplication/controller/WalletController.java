package ru.example.walletapplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.walletapplication.dto.WalletMapper;
import ru.example.walletapplication.dto.WalletRequest;
import ru.example.walletapplication.service.WalletService;

@RestController
@RequestMapping("api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @PostMapping()
    public ResponseEntity<?> createWallet() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(walletMapper.toWalletResponse(
                        walletService.createWallet()));
    }

    @GetMapping("transaction")
    public ResponseEntity<?> getBalance(@RequestBody WalletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletMapper.toWalletResponse(
                        walletService.getWallet(request.getId())));
    }

    @PostMapping("transaction/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody WalletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletMapper.toWalletResponse(
                        walletService.deposit(request.getId(), request.getAmount())));
    }

    @PostMapping("transaction/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WalletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletMapper.toWalletResponse(
                        walletService.withdraw(request.getId(), request.getAmount())));
    }
}
