package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AuthResponse;
import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.model.dto.user.UsernameUpdateDTO;
import com.example.moneyapp2.model.dto.user.UsernameUpdateEmailDTO;
import com.example.moneyapp2.service.TokenService;
import com.example.moneyapp2.service.UserProfileService;
import com.example.moneyapp2.service.UserService;
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
    private final TokenService tokenService;

    @GetMapping("/profile")
    public ResponseEntity<?> userProfile(Principal principal) {

        UserProfileDTO profileInfo = this.userProfileService.provideUserProfileData(principal.getName());


        return ResponseEntity.ok(profileInfo);
    }

    @PatchMapping("/updateUsername")
    public ResponseEntity<?> updateUsername(@Valid @RequestBody UsernameUpdateDTO updateUsernameDTO, Principal principal) {

        UserProfileDTO updatedUser = this.userProfileService.updateUsername(principal.getName(), updateUsernameDTO.getUsername());
        Authentication authentication = this.userProfileService.authenticateUser(updatedUser.getUsername(), updateUsernameDTO.getPassword());
        String token = this.tokenService.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PatchMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@Valid @RequestBody UsernameUpdateEmailDTO updateEmailDTO, Principal principal) {

        this.userProfileService.updateEmail(principal.getName(), updateEmailDTO.getEmail());

        return new ResponseEntity<>("Changed email", HttpStatus.OK);
    }
}
