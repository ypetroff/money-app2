package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AuthResponse;
import com.example.moneyapp2.model.dto.user.UserLoginDTO;
import com.example.moneyapp2.model.dto.user.UserRegisterDTO;
import com.example.moneyapp2.service.TokenService;
import com.example.moneyapp2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {

        this.userService.registerUser(userRegisterDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndCreateToken(@RequestBody UserLoginDTO userLoginDTO) {

        Authentication authentication =
                this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLoginDTO.getUsername(), userLoginDTO.getPassword()));

        String token = this.tokenService.generateAccessToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
