package com.example.moneyapp2.model.dto.saving;

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
public class SavingDetailsDTO {

    private Long id;

    private BigDecimal amount;

    private LocalDateTime dateOfCreation;

    private LocalDate endDate;

    private String goal;

    private List<String> owners;

    private List<String> contributors;
}
