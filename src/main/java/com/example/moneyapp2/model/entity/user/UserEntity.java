package com.example.moneyapp2.model.entity.user;

import com.example.moneyapp2.model.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    public UserEntity() {
        this.userRoles = new ArrayList<>();
        this.income = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.savings = new ArrayList<>();
    }

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<UserRoleEntity> userRoles;

    @OneToMany
    private List<IncomeEntity> income;

    @OneToMany
    private List<ExpenseEntity> expenses;

    @ManyToMany
    private List<SavingEntity> savings;


    public void addRole(UserRoleEntity role) {
        this.userRoles.add(role);
    }

    public void addIncome(IncomeEntity income) {
        this.income.add(income);
    }
}
