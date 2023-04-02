package com.example.moneyapp2.service;

import com.example.moneyapp2.repository.DebitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DebitService {

    private final DebitRepository debitRepository;

    public BigDecimal getTotalDebitInTheApp() {
        return getTotalCash().add(getTotalCardFunds());
    }

    private BigDecimal getTotalCash() {
        return this.debitRepository.allDebitCashSum()
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotalCardFunds() {
        return this.debitRepository.allDebitCardSum()
                .orElse(BigDecimal.ZERO);
    }

}

