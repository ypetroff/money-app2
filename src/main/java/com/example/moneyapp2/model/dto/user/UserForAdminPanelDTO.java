package com.example.moneyapp2.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
