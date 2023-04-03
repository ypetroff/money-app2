package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.AccountDashboardDTO;
import com.example.moneyapp2.model.dto.CreditDataDTO;
import com.example.moneyapp2.model.dto.DebitDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final DebitService debitService;
    private final CreditService creditService;

    private final UserService userService;

    public AccountDashboardDTO getUserAccountInfo(String username) {

        DebitDataDTO debit = this.debitService.getDebitOfUser(username);
        CreditDataDTO credit = this.creditService.getCreditOfUser(username);


        return AccountDashboardDTO.builder()
                .cash(debit.getCash())
                .card(debit.getCard())
                .credit(credit.getCredit())
                .build();
    }

}
