package com.example.moneyapp2.web;

import com.example.moneyapp2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @RequestMapping("/")
    public String home() {
        // This is the home endpoint for unauthenticated users
        return "unauthenticated-home";
    }

    @RequestMapping("/home")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public ResponseEntity<?> authenticatedHome() {
        // This is the home endpoint for authenticated users
        return "authenticated-home";
    }
}
