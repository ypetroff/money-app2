package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.expense.CreateExpenseDTO;
import com.example.moneyapp2.model.dto.income.CreateIncomeDTO;
import com.example.moneyapp2.model.dto.income.IncomeDetailsDTO;
import com.example.moneyapp2.model.dto.income.IncomeInfoDTO;
import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.saving.SavingDetailsDTO;
import com.example.moneyapp2.model.dto.saving.SavingInfoDTO;
import com.example.moneyapp2.service.SavingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/saving")
@RequiredArgsConstructor
public class SavingController {

    private final SavingService savingService;

    @GetMapping("/all")
    public ResponseEntity<List<SavingInfoDTO>> allSavingsOfTheUser(Principal principal) {
        return ResponseEntity.ok(this.savingService.getAllSavingOfUser(principal.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<List<SavingInfoDTO>> addNewSaving(@Valid @RequestBody CreateSavingDTO savingDTO, Principal principal) {

        return ResponseEntity.ok(this.savingService.addNewSavingAndReturnAllSavingsOfUser(savingDTO, principal.getName()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<SavingDetailsDTO> detailedSavingInfo(@PathVariable Long id) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(this.savingService.getDetailsOfSaving(id));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<SavingDetailsDTO> editSavingInfo(@PathVariable Long id,
                                                           @Valid @RequestBody CreateSavingDTO changedSavingInfo) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(this.savingService.editSaving(id, changedSavingInfo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        this.savingService.deleteSaving(id);

        return ResponseEntity.noContent().build();
    }
}
