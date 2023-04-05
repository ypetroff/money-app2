package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.saving.CreateSavingDTO;
import com.example.moneyapp2.model.dto.saving.EditSavingDTO;
import com.example.moneyapp2.model.dto.saving.SavingDetailsDTO;
import com.example.moneyapp2.model.dto.saving.SavingInfoDTO;
import com.example.moneyapp2.service.SavingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> addNewSaving(@Valid @RequestBody CreateSavingDTO savingDTO, Principal principal) {

        if(isWithoutOwnerAndContributor(savingDTO)) {
            return ResponseEntity.badRequest().body("Saving should have owner and contributor");
        }

        return ResponseEntity.ok(this.savingService.addNewSavingAndReturnAllSavingsOfUser(savingDTO, principal.getName()));
    }

    private static boolean isWithoutOwnerAndContributor(CreateSavingDTO savingDTO) {
        return savingDTO.getOwners().size() == 0 || savingDTO.getContributors().size() == 0 ||
                savingDTO.getOwners().get(0).length() == 0 || savingDTO.getContributors().get(1).length() == 0;
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<SavingDetailsDTO> detailedSavingInfo(@PathVariable Long id, Principal principal) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.savingService.unauthorizedView(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.savingService.getDetailsOfSaving(id));
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<EditSavingDTO> editSavingInfo(@PathVariable Long id,
                                                        Principal principal) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.savingService.unauthorizedToModify(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.savingService.getSingleSaving(id));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<SavingDetailsDTO> editSavingInfo(@PathVariable Long id,
                                                           @Valid @RequestBody CreateSavingDTO changedSavingInfo,
                                                           Principal principal) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.savingService.unauthorizedToModify(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(this.savingService.editSaving(id, changedSavingInfo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id, Principal principal) {

        if(this.savingService.SavingNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(this.savingService.unauthorizedToModify(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        this.savingService.deleteSaving(id);

        return ResponseEntity.noContent().build();
    }
}
