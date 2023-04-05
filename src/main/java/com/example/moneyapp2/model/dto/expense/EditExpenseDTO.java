package com.example.moneyapp2.model.dto.expense;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditExpenseDTO {

    private Long id;
    @Size(min = 2, max = 200)
    @NotEmpty
    private String name;

    private BigDecimal pricePerUnit;

    private Integer numberOfUnits;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal totalPrice;

    @NotEmpty
    private String timeOfPurchase;

    @NotEmpty
    private String category;
}
