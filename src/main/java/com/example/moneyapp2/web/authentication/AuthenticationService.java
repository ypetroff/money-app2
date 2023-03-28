package com.example.moneyapp2.web.authentication;

import com.example.moneyapp2.model.entity.UserEntity;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRepository;
import com.example.moneyapp2.security.ApplicationUserDetailsService;
import com.example.moneyapp2.security.JWT.JWT_Service;
import com.example.moneyapp2.model.entity.Token;
import com.example.moneyapp2.repository.TokenRepository;
import com.example.moneyapp2.model.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWT_Service jwtService;
  private final AuthenticationManager authenticationManager;
  private final ApplicationUserDetailsService applicationUserDetailsService;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = UserEntity.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    user.addRole(new UserRoleEntity(UserRole.USER));
    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(applicationUserDetailsService.loadUserByUsername(user.getUsername()));
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(applicationUserDetailsService.loadUserByUsername(user.getUsername()));
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  private void saveUserToken(UserEntity user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.saveAndFlush(token);
  }

  private void revokeAllUserTokens(UserEntity user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAllAndFlush(validUserTokens);
  }
}
