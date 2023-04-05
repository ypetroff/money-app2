package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "savings")
public class SavingEntity extends BaseEntity {

    public SavingEntity() {
        this.owners = new ArrayList<>();
        this.contributors = new ArrayList<>();
    }

    @Column(name = "amount", columnDefinition = "DECIMAL default 0")
    private BigDecimal amount;

    @Column(name = "date_of_creation", nullable = false)
    private LocalDateTime dateOfCreation;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "goal", columnDefinition = "VARCHAR(255) default 'not provided by user'")
    private String goal;

    @ManyToMany
    private List<UserEntity> owners;

    @ManyToMany
    private List<UserEntity> contributors;

    public void addOwner(UserEntity owner) {
        this.owners.add(owner);
    }

    public void addContributor(UserEntity contributor) {
        this.contributors.add(contributor);
    }

}
