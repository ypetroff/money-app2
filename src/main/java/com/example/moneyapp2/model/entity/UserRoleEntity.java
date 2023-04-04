package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_roles")
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole userRole;

    @Override
    public String toString() {
        return this.userRole.name();
    }
}
