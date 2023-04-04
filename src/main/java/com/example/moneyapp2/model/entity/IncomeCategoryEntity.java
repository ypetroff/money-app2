package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.enums.IncomeCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "income_categories")
public class IncomeCategoryEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private IncomeCategory category;

    @Override
    public String toString() {
        return this.category.name();
    }
}

