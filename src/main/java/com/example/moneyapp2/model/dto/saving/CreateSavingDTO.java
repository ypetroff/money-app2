package com.example.moneyapp2.model.dto.saving;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSavingDTO {

    @Digits(integer = 10, fraction = 2, message = "Incorrect amount. Check if all symbols are valid numbers and you are using a decimal point")
    private BigDecimal amount;

    @PastOrPresent(message = "The date should be past or present")
    private LocalDateTime dateOfCreation;

    @Future(message = "This date should be in the future")
    private LocalDate endDate;

    private String goal;

    @NotNull(message = "At least one user should be an owner")
    private List<String> owners;

    @NotNull(message = "At least one user should be a contributor")
    private List<String> contributors;
}
