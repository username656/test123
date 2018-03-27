package com.aurea.kayako.api.security;

import static lombok.AccessLevel.PACKAGE;

import com.aurea.kayako.api.model.User;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {

    @Getter(PACKAGE)
    private final User user;

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * @return Always true because the accounts do not expire
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return Always true because the accounts do not get locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return Always true because the credentials do not expire
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }

}
