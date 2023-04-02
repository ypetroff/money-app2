package com.example.moneyapp2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class ExpenseEntity extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    //TODO: When the user inputs totalPrice, pricePerUnit and numberOfUnits could be null
    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;

    @Column(name = "number_of_units")
    private Integer numberOfUnits;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    //NOTE: another option - set a default param for expenseDTO OR set default in the controller
    @Column(name = "date_of_purchase", columnDefinition = "TIMESTAMP default NOW()")
    private LocalDateTime timeOfPurchase;

    @ManyToOne
    private ExpenseCategoryEntity category;
}

