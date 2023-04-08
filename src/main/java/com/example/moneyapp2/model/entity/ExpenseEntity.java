package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
public class ExpenseEntity extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;

    @Column(name = "number_of_units")
    private Integer numberOfUnits;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "date_of_purchase", nullable = false)
    private LocalDateTime timeOfPurchase;

    @ManyToOne
    private ExpenseCategoryEntity category;

    @ManyToOne
    private UserEntity owner;
}

