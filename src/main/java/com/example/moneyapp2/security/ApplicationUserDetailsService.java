package com.example.moneyapp2.security;

import com.example.moneyapp2.model.entity.UserEntity;
import com.example.moneyapp2.model.entity.UserRoleEntity;
import com.example.moneyapp2.model.entity.user.MoneyAppUserDetails;
import com.example.moneyapp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByEmail(username)
                .map(this::map)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("Username %s, not found!", username)));
    }

    private UserDetails map(UserEntity userEntity) {
        return MoneyAppUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(extractAuthorities(userEntity))
                .build();

    }

    private Collection<GrantedAuthority> extractAuthorities(UserEntity userEntity) {
        return userEntity
                .getUserRoles()
                .stream()
                .map(this::mapRole)
                .toList();
    }

    private GrantedAuthority mapRole(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getUserRole().name());
    }
}
