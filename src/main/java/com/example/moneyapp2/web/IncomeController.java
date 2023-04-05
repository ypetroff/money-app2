package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.income.EditIncomeDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<IncomeInfoDTO>> addNewIncome(@Valid @RequestBody CreateIncomeDTO incomeDTO, Principal principal) {

        return ResponseEntity.ok(this.incomeService.addNewIncomeAndReturnAllIncomeOfUser(incomeDTO, principal.getName()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<IncomeDetailsDTO> detailedIncomeInfo(@PathVariable Long id, Principal principal) {

        if(this.incomeService.IncomeNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.incomeService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.incomeService.getDetailsOfIncome(id));
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<EditIncomeDTO> editIncomeInfo(@PathVariable Long id,
                                                        Principal principal) {

        if(this.incomeService.IncomeNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.incomeService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.incomeService.getSingleIncome(id));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<IncomeDetailsDTO> editIncomeInfo(@PathVariable Long id,
                                                          @Valid @RequestBody CreateIncomeDTO changedIncomeInfo,
                                                           Principal principal) {

        if(this.incomeService.IncomeNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.incomeService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.incomeService.editIncome(id, changedIncomeInfo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id, Principal principal) {

        if(this.incomeService.IncomeNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.incomeService.unauthorizedUser(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        this.incomeService.deleteIncome(id);

        return ResponseEntity.noContent().build();
    }
}
