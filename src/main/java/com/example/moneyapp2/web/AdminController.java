package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AdminDashboardDTO;
import com.example.moneyapp2.service.AdminService;
import com.example.moneyapp2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> moneyAppInfo() {

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(this.adminService.provideAdminDashboardData());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        if(this.userService.isNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(id == 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        this.userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/promote/{id}")
    public ResponseEntity<AdminDashboardDTO> makeAdmin(@PathVariable Long id) {

        if(this.userService.isNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        this.userService.makeAdmin(id);

        return ResponseEntity.ok(this.adminService.provideAdminDashboardData());
    }

    @PatchMapping("/demote/{id}")
    public ResponseEntity<AdminDashboardDTO> removeAdminRights(@PathVariable Long id) {

        if(this.userService.isNotPresent(id)) {
            return ResponseEntity.notFound().build();
        }

        if(id == 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        this.userService.removeAdminRights(id);

        return ResponseEntity.ok(this.adminService.provideAdminDashboardData());
    }

}
