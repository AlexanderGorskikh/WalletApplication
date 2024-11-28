package ru.example.walletapplication;



import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.example.walletapplication.controller.WalletController;
import ru.example.walletapplication.entity.Wallet;
import ru.example.walletapplication.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WalletService walletService;

    @Test
    void testCreateWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.ZERO);

        Mockito.when(walletService.createWallet()).thenReturn(wallet);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect((ResultMatcher) jsonPath("$.balance").value("0.0"));
    }

    @Test
    void testGetWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new BigDecimal("100.00"));

        Mockito.when(walletService.getWallet(walletId)).thenReturn(wallet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/" + walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(walletId.toString()))
                .andExpect((ResultMatcher) jsonPath("$.balance").value("100.0"));
    }

    @Test
    void testDeposit() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new BigDecimal("150.00"));

        Mockito.when(walletService.deposit(walletId, new BigDecimal("50.00"))).thenReturn(wallet);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets/" + walletId + "/transactions/deposit")
                        .param("amount", "50.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(walletId.toString()))
                .andExpect((ResultMatcher) jsonPath("$.balance").value("150.0"));
    }

    @Test
    void testWithdraw() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new BigDecimal("50.00"));

        Mockito.when(walletService.withdraw(walletId, new BigDecimal("50.00"))).thenReturn(wallet);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets/" + walletId + "/transactions/withdraw")
                        .param("amount", "50.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(walletId.toString()))
                .andExpect((ResultMatcher) jsonPath("$.balance").value("50.0"));
    }
}
