package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AccountDashboardDTO;
import com.example.moneyapp2.service.AccountService;
import com.example.moneyapp2.service.CreditService;
import com.example.moneyapp2.service.DebitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/info")
    public ResponseEntity<?> account(Principal principal) {

        AccountDashboardDTO accountInfo = this.accountService.getUserAccountInfo(principal.getName());

        return ResponseEntity.ok(accountInfo);
    }
}
