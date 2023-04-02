package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.user.UserInfoDTO;
import com.example.moneyapp2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> home() {

        return ResponseEntity.ok( "Welcome to the unauthenticated home page!");
    }

    @GetMapping
    @RequestMapping("/home")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public ResponseEntity<?> authenticatedHome(Principal principal) {

        UserInfoDTO currentUser = this.userService.provideUserDashboardData(principal.getName());

        return ResponseEntity.ok(currentUser);
    }
}
