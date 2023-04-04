package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.AuthResponse;
import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.model.dto.user.UserUpdateEmailDTO;
import com.example.moneyapp2.model.dto.user.UserUpdatePasswordDTO;
import com.example.moneyapp2.model.dto.user.UserUpdateUsernameDTO;
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
@RequestMapping("api/users/update")
@RequiredArgsConstructor
public class UpdateUserProfileController {

    private final UserProfileService userProfileService;
    private final TokenService tokenService;

    @PatchMapping("/username")
    public ResponseEntity<?> updateUsername(@Valid @RequestBody UserUpdateUsernameDTO updateUsernameDTO, Principal principal) {

        UserProfileDTO updatedUser = this.userProfileService.updateUsername(principal.getName(), updateUsernameDTO.getUsername());
        Authentication authentication = this.userProfileService.authenticateUser(updatedUser.getUsername(), updateUsernameDTO.getPassword());
        String token = this.tokenService.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PatchMapping("/email")
    public ResponseEntity<?> updateEmail(@Valid @RequestBody UserUpdateEmailDTO updateEmailDTO, Principal principal) {

        this.userProfileService.updateEmail(principal.getName(), updateEmailDTO.getEmail());

        return new ResponseEntity<>("Changed email", HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserUpdatePasswordDTO updatePasswordDTO, Principal principal) {

        this.userProfileService.updatePassword(updatePasswordDTO, principal.getName());

        return new ResponseEntity<>("Changed password", HttpStatus.OK);
    }
}
