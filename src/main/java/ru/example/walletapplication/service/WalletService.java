package ru.example.walletapplication.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.walletapplication.entity.Wallet;
import ru.example.walletapplication.exception.EntityNotFoundException;
import ru.example.walletapplication.exception.InsufficientFundsException;
import ru.example.walletapplication.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet getWallet(UUID id) {
        return walletRepository.findByIdWithLock(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found: " + id));
    }

    @Transactional
    public Wallet deposit(UUID id, BigDecimal amount) {
        Wallet wallet = getWallet(id);
        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet withdraw(UUID id, BigDecimal amount) {
        Wallet wallet = getWallet(id);
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        return walletRepository.save(wallet);
    }
}
