package com.example.moneyapp2.model.dto.user;

import com.example.moneyapp2.model.entity.UserRoleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private List<String> userRoles;
}
