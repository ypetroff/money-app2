package com.example.moneyapp2.service;

import com.example.moneyapp2.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    public BigDecimal getTotalCreditInTheApp() {
        return this.creditRepository.allCreditSum().orElse(BigDecimal.ZERO);
    }
}
