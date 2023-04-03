package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debit")
public class DebitEntity extends BaseEntity{

    @Column(name = "cash", columnDefinition = "DECIMAL default 0")
    private BigDecimal cash;

    @Column(name = "card", columnDefinition = "DECIMAL default 0")
    private BigDecimal card;

    @OneToOne
    UserEntity owner;
}
