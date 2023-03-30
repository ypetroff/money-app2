package com.example.moneyapp2.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> moneyAppInfo() {
        return ResponseEntity.ok("This works");
    }

}
