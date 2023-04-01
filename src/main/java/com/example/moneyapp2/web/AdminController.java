package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AdminDashboardDTO;
import com.example.moneyapp2.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> moneyAppInfo() {

        AdminDashboardDTO adminDashboardDTO = this.adminService.provideDashboardData();

        return ResponseEntity.ok(adminDashboardDTO);
    }

}
