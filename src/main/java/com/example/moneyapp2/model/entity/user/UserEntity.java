package com.example.moneyapp2.model.entity.user;

import com.example.moneyapp2.model.entity.BaseEntity;
import com.example.moneyapp2.model.entity.CreditEntity;
import com.example.moneyapp2.model.entity.DebitEntity;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    public UserEntity() {
        this.userRoles = new ArrayList<>();
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


    public void addRole(UserRoleEntity role) {
        this.userRoles.add(role);
    }

}
