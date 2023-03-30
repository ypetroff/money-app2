package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity
@Table(name = "credit")
public class CreditEntity extends BaseEntity {

    public CreditEntity() {
        this.debtors = new ArrayList<>();
    }

    @Column(name = "creditor")
    private String creditor;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    @ManyToMany
    private List<UserEntity> debtors;
}
