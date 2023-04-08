package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AuthResponse;
import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.model.dto.user.UserUpdatePasswordDTO;
import com.example.moneyapp2.model.dto.user.UserUpdateUsernameDTO;
import com.example.moneyapp2.model.dto.user.UserUpdateEmailDTO;
import com.example.moneyapp2.service.TokenService;
import com.example.moneyapp2.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> userProfile(Principal principal) {

        UserProfileDTO profileInfo = this.userProfileService.provideUserProfileData(principal.getName());


        return ResponseEntity.ok(profileInfo);
    }
}
