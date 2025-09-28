package com.kimhok.tickets.config.security;

import com.kimhok.tickets.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;
    private static final String DEFAULT_ROLE = "USER";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getRoleName();
        if (roleName == null || roleName.isEmpty()) {
            log.warn("User {} has no role assigned, using default role: {}", user.getEmail(), DEFAULT_ROLE);
            roleName = DEFAULT_ROLE;
        }
        return Set.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() != null && user.getStatus().name().equals("ACTIVE");
    }

    public User getUser() {
        return user;
    }
}