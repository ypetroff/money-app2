package com.example.moneyapp2.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Mock private JwtEncoder mockEncoder;

  @Mock private AuthenticationManager mockAuthManager;

  private TokenService toTest;

  @BeforeEach
  void setUp() {
    toTest = new TokenService(this.mockEncoder);
  }

  @Test
  void generateAccessToken() {
//    Instant now = Instant.now();
//
//    String scope = "USER";
//
//    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(scope));
//
//    Authentication authentication =
//        new UsernamePasswordAuthenticationToken("test-user", "test-password", authorities);
//
//    JwtClaimsSet claims =
//        JwtClaimsSet.builder()
//            .issuer("self")
//            .issuedAt(now)
//            .expiresAt(now.plus(1, ChronoUnit.HOURS))
//            .subject(authentication.getName())
//            .claim("scope", scope)
//            .build();
//
//    when(this.mockEncoder.encode(JwtEncoderParameters.from(claims))).thenReturn(any());
//
//    toTest.generateAccessToken(authentication);
//
//    verify(this.mockEncoder).encode(any());
  }
}
