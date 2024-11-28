package ru.example.walletapplication.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.example.walletapplication.entity.Wallet;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletMapper {
    WalletResponse toWalletResponse(Wallet wallet);
}
