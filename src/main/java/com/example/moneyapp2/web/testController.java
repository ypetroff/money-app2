//package com.example.moneyapp2.web;
//
//import com.example.moneyapp2.model.dto.AuthRequest;
//import com.example.moneyapp2.model.entity.user.UserEntity;
//import com.example.moneyapp2.service.JwtService;
//import com.example.moneyapp2.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/auth")
//public class testController {
//
//    private final JwtService jwtService;
//    private final UserService userService;
//
//    @PostMapping("/register")
//    public String messageSuccess() {
//        return "This is without login";
//    }
//
//    @GetMapping("/demo")
//    public ResponseEntity<String> sayHello() {
//        return ResponseEntity.ok("Hello from secured endpoint");
//    }
//
//    @PostMapping("/authenticate")
//    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
//            return jwtService.generateToken(authRequest.getUsername());
//    }
//}
