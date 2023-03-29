package com.example.moneyapp2.model.entity;

import com.example.moneyapp2.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

//@Data
//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_role")
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole userRole;
}
