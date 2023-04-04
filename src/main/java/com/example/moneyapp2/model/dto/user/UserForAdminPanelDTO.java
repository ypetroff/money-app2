package com.example.moneyapp2.model.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForAdminPanelDTO {

    private Long id;
    private String username;


    private List<String> roles;

}
