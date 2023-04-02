package com.example.moneyapp2.web;

import com.example.moneyapp2.model.dto.user.UserProfileDTO;
import com.example.moneyapp2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> userProfile(Principal principal) {

        UserProfileDTO profileInfo = this.userService.provideUserProfileData(principal.getName());

        return ResponseEntity.ok(profileInfo);
    }

    @PatchMapping("/updateUsername")
    public ResponseEntity<?> updateUsername(@RequestBody String username, Principal principal) {

       UserProfileDTO updatedUser =  this.userService.updateUsername(principal.getName(), username);

        return ResponseEntity.ok(updatedUser);
    }
}
