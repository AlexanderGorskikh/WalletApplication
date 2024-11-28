package ru.example.walletapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.example.walletapplication.entity.Wallet;
import ru.example.walletapplication.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("integration")
public class ControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
    }

    @Test
    void testCreateWallet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.balance").value("0"));
    }

    @Test
    void testGetBalance() throws Exception {
        Wallet wallet = new Wallet();
        wallet = walletRepository.save(wallet);
        Map<String, String> requestBody = Map.of(
                "id", wallet.getId().toString(),
                "balance", wallet.getBalance().toString());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/transaction")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value("0.0"));
    }

    @Test
    void testDeposit() throws Exception {
        Wallet wallet = new Wallet();
        wallet = walletRepository.save(wallet);
        Map<String, String> requestBody = Map.of(
                "id", wallet.getId().toString(),
                "amount", "100");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets/transaction/deposit")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("100.0"));
    }

    @Test
    void testWithdraw() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100.00"));
        wallet = walletRepository.save(wallet);
        Map<String, String> requestBody = Map.of(
                "id", wallet.getId().toString(),
                "amount", "50");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets/transaction/withdraw")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("50.0"));
    }

    @Test
    void testInsufficientFunds() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("10.00"));
        wallet = walletRepository.save(wallet);
        Map<String, String> requestBody = Map.of(
                "id", wallet.getId().toString(),
                "amount", "50");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets/transaction/withdraw")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Insufficient funds."));
    }
}
