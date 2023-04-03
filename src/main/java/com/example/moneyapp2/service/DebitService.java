package com.example.moneyapp2.service;

import com.example.moneyapp2.exception.NoAvailableDataException;
import com.example.moneyapp2.model.dto.AccountDashboardDTO;
import com.example.moneyapp2.model.dto.DebitDataDTO;
import com.example.moneyapp2.model.entity.DebitEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.repository.DebitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public DebitDataDTO getDebitOfUser(String username) {

        DebitEntity entity = this.debitRepository.findByOwnerUsername(username)
                .orElseThrow(() -> new NoAvailableDataException("No valid name provided by Principal"));

        BigDecimal cash = entity.getCash();
        BigDecimal card = entity.getCard();

        return new DebitDataDTO(card, cash);
    }


}

