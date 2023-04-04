package com.example.moneyapp2.model.dto.saving;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
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

    @Digits(integer = 10, fraction = 2)
    @NotEmpty
    private BigDecimal amount;

    @PastOrPresent
    @NotEmpty
    private LocalDateTime dateOfCreation;

    @Future
    private LocalDate endDate;

    private String goal;

    @NotEmpty
    private List<String> owners;

    @NotEmpty
    private List<String> contributors;
}
