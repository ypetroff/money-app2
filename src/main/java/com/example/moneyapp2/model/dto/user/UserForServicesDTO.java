package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.model.entity.UserRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForServicesDTO {

    private String id;
    private String password;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private List<UserRoleEntity> userRoles;
}
