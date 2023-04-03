package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.model.entity.UserRoleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserForServicesDTO {

    private String password;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private List<UserRoleEntity> userRoles;
}
