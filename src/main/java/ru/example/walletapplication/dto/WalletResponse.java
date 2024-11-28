package ru.example.walletapplication.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class WalletResponse {
    private UUID id;
    private BigDecimal balance;
}
