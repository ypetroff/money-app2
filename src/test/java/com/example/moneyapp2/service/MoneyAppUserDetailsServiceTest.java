package com.example.moneyapp2.service;

import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.entity.user.UserEntity;
import com.example.moneyapp2.model.enums.UserRole;
import com.example.moneyapp2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoneyAppUserDetailsServiceTest {

  @Mock private UserRepository mockUserRepository;

  private MoneyAppUserDetailsService toTest;

  @BeforeEach
  void setUp() {
    toTest = new MoneyAppUserDetailsService(this.mockUserRepository);
  }

  @Test
  void loadUserByUsernameUserExists() {
    UserEntity user =
        UserEntity.builder()
            .username("test-username")
            .password("test_password")
            .firstName("test-first-name")
            .lastName("test-last-name")
            .email("test@test.com")
            .userRoles(List.of(new UserRoleEntity(UserRole.USER)))
            .build();
    user.setId(1L);

    when(this.mockUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

    UserDetails actualUserDetails = toTest.loadUserByUsername(user.getUsername());

    assertEquals(user.getUsername(), actualUserDetails.getUsername());
    assertTrue(
        actualUserDetails
            .getAuthorities()
            .contains(new SimpleGrantedAuthority(user.getUserRoles().get(0).getUserRole().name())));
  }

  @Test
  void loadUserByUsernameUserDoesNotExists() {
   assertThrows(UsernameNotFoundException.class, () -> toTest.loadUserByUsername("test-username"));
  }
}
