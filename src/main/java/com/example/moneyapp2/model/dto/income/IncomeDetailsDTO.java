package com.example.moneyapp2.model.dto.income;

import com.example.moneyapp2.model.entity.IncomeCategoryEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDetailsDTO {

    private Long id;

    private BigDecimal amount;

    private String description;

    private String createdOn;

    private String incomeCategory;

}
