package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AuthResponse;
import com.example.moneyapp2.model.dto.UserLoginDTO;
import com.example.moneyapp2.model.dto.UserRegisterDTO;
import com.example.moneyapp2.service.JwtService;
import com.example.moneyapp2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class authController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {

        this.userService.registerUser(userRegisterDTO);

        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndCreateToken(@RequestBody UserLoginDTO userLoginDTO) {

        this.userService.checkLoginCredentials(userLoginDTO);

        String token = this.jwtService.generateToken(userLoginDTO.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
