package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "income")
public class IncomeEntity extends BaseEntity{

    @Column(name = "amount", columnDefinition = "DECIMAL(10 , 2) default 0", nullable = false)
    private BigDecimal amount;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @ManyToOne
    private IncomeCategoryEntity incomeCategory;

    @OneToOne
    private UserEntity owner;
}
