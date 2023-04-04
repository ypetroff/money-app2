package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping("/all")
    public ResponseEntity<List<IncomeInfoDTO>> allIncomeOfTheUser(Principal principal) {
        return ResponseEntity.ok(this.incomeService.getAllIncomeOfUser(principal.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<  List<IncomeInfoDTO>> addNewIncome(@Valid @RequestBody CreateIncomeDTO addIncomeDTO, Principal principal) {

        return ResponseEntity.ok(this.incomeService.addNewIncomeAndReturnAllIncomeOfUser(addIncomeDTO, principal.getName()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<IncomeDetailsDTO> detailedIncomeInfo(@PathVariable Long id) {
        return ResponseEntity.ok(this.incomeService.getDetailsOfIncome(id));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<IncomeDetailsDTO> editIncomeInfo(@PathVariable Long id,
                                                          @RequestBody CreateIncomeDTO changedIncomeInfo) {
        return ResponseEntity.ok(this.incomeService.editIncome(id, changedIncomeInfo));
    }
}
