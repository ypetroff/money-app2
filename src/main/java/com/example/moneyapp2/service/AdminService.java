package com.example.moneyapp2.service;

import com.example.moneyapp2.model.dto.AdminDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;

//    public AdminDashboardDTO generateDashboardInfo() {
//        return AdminDashboardDTO.builder()
//                .totalUsersCount(this.userService.getTotalNumberOfAppUsers())
//                .
//                .build();
//    }
}
