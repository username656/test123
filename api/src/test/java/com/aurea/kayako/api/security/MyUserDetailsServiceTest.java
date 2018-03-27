package com.aurea.kayako.api.security;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aurea.kayako.api.model.User;
import com.aurea.kayako.api.repositories.UserRepository;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsServiceTest {

    private static final String SAMPLE_USERNAME = "sample";

    @Test
    public void shouldReturnTheDataFromTheUserRepository() {
        User user = EnhancedRandom.random(User.class);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(user);
        MyUserDetailsService object = new MyUserDetailsService(userRepository);

        UserDetails result = object.loadUserByUsername(user.getUsername());

        assertThat(result.getUsername(), is(user.getUsername()));
        assertThat(result.getPassword(), is(user.getPassword()));
        assertThat(result.isEnabled(), is(user.isEnabled()));
        assertThat(result.getAuthorities(), is(user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList())));
    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenNotFoundInUserRepository() {
        try {
            UserRepository userRepository = mock(UserRepository.class);
            when(userRepository.findOneByUsername(SAMPLE_USERNAME)).thenReturn(null);
            MyUserDetailsService object = new MyUserDetailsService(userRepository);

            object.loadUserByUsername(SAMPLE_USERNAME);
            fail("It should throw an exception since the user is not found");
        } catch (UsernameNotFoundException ex) {
            assertThat(ex.getMessage(), is("The user " + SAMPLE_USERNAME + " can not be found."));
        }
    }

}
