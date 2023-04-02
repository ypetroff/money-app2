package com.example.moneyapp2.model.entity.user;

import com.example.moneyapp2.model.entity.*;
import jakarta.persistence.*;
import lombok.*;

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
        this.credits = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<UserRoleEntity> userRoles;

    @OneToOne
    private DebitEntity debit;

    @ManyToMany
    private List<CreditEntity> credits;

    @OneToMany
    private List<ExpenseEntity> expenses;


    public void addRole(UserRoleEntity role) {
        this.userRoles.add(role);
    }

    public void addCredit(CreditEntity credit) {
        this.credits.add(credit);
    }

    public void addExpense(ExpenseEntity expense) {
        this.expenses.add(expense);
    }

}
