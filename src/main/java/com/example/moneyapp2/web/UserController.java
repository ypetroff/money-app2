package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AuthResponse;
import com.example.moneyapp2.model.dto.user.UpdatedUserProfileDTO;
import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.model.dto.user.UsernameUpdateDTO;
import com.example.moneyapp2.service.TokenService;
import com.example.moneyapp2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/profile")
    public ResponseEntity<?> userProfile(Principal principal) {

        UserProfileDTO profileInfo = this.userService.provideUserProfileData(principal.getName());


        return ResponseEntity.ok(profileInfo);
    }

    @PatchMapping("/updateUsername")
    public ResponseEntity<?> updateUsername(@RequestBody UsernameUpdateDTO updateUsernameDTO, Principal principal) {

        UserProfileDTO updatedUser = this.userService.updateUsername(principal.getName(), updateUsernameDTO.getUsername());
        Authentication authentication = this.userService.authenticateUser(updatedUser.getUsername(), updateUsernameDTO.getPassword());
        String token = this.tokenService.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }
    //todo: change email and password functionalities
}
