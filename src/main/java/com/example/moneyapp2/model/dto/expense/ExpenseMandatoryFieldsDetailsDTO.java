package com.example.moneyapp2.model.dto.expense;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseMandatoryFieldsDetailsDTO {

    private String name;

    private BigDecimal totalPrice;

    private LocalDateTime timeOfPurchase;

    private String category;
}
