package com.example.moneyapp2.model.dto.user;

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
