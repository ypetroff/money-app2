package com.example.moneyapp2.scheduler;

import com.example.moneyapp2.service.ExpenseService;
import com.example.moneyapp2.service.IncomeService;
import com.example.moneyapp2.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Maintenance {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final SavingService savingService;

    @Scheduled(cron = "0 0 0 * 1-12 *")
    public void RemoveOldExpenses() {
        this.expenseService.maintenance();
    }

    @Scheduled(cron = "0 3 0 * 1-12 *")
    public void RemoveOldIncome() {
        this.incomeService.maintenance();
    }

    @Scheduled(cron = "0 6 0 * 1-12 *")
    public void DistributeDueSavings() {
        this.savingService.maintenance();
    }
}
