package com.example.moneyapp2.model.dto.saving;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingInfoDTO {

    private Long id;

    private BigDecimal amount;

    private LocalDate endDate;

    private String goal;
}
