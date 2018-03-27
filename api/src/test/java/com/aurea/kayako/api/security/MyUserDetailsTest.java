package com.aurea.kayako.api.security;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.aurea.kayako.api.model.User;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MyUserDetailsTest {

    @Test
    public void itShouldReturnTheUserDataForSpecificFields() {
        User user = EnhancedRandom.random(User.class);

        MyUserDetails object = new MyUserDetails(user);

        assertThat(object.getUsername(), is(user.getUsername()));
        assertThat(object.getPassword(), is(user.getPassword()));
        assertThat(object.isEnabled(), is(user.isEnabled()));
        assertThat(object.getAuthorities(), is(user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList())));
    }

    public void itShourlReturnTrueForSpecificFields() {
        User user = EnhancedRandom.random(User.class);

        MyUserDetails object = new MyUserDetails(user);

        assertThat(object.isAccountNonExpired(), is(true));
        assertThat(object.isAccountNonLocked(), is(true));
        assertThat(object.isCredentialsNonExpired(), is(true));
    }

}
