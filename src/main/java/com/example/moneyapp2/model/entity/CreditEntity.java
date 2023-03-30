package com.example.moneyapp2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credit")
public class CreditEntity extends BaseEntity {

    @Column(name = "creditor")
    private String creditor;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;
}
