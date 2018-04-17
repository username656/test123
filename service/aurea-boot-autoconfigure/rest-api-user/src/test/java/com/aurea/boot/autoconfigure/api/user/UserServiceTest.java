package com.aurea.boot.autoconfigure.api.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aurea.boot.autoconfigure.api.error.ResetTokenInvalidException;
import com.aurea.boot.autoconfigure.api.user.impl.UserServiceImpl;
import com.aurea.boot.autoconfigure.api.user.json.TokenPasswordJson;
import com.aurea.boot.autoconfigure.data.user.User;
import com.aurea.boot.autoconfigure.data.user.UserRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    private static final String USERNAME = "test@test.com";
    private static final String RESET_KEY = "key123";
    private static final String PASSWORD = "pass";
    private static final String ENCODED_PASSWORD = "ssap!@#$";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService userService = new UserServiceImpl(userRepository, passwordEncoder);

    @Test
    public void getCurrentUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(
                Optional.of(User.builder().username(USERNAME).build()));
        User user = userService.getCurrentUser(USERNAME);
        assertEquals(USERNAME, user.getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getCurrentUserException() {
        when(userRepository.findByUsername(USERNAME)).thenThrow(
                new EntityNotFoundException("Entity not found"));
        userService.getCurrentUser(USERNAME);
    }

    @Test
    public void resetPassword() {
        when(userRepository.findByResetKey(RESET_KEY)).thenReturn(
                Optional.of(User.builder().username(USERNAME).build()));
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        TokenPasswordJson tokenPasswordJson = new TokenPasswordJson(RESET_KEY, PASSWORD);
        userService.resetPassword(tokenPasswordJson);
        verify(userRepository).findByResetKey(RESET_KEY);
        verify(passwordEncoder).encode(PASSWORD);
    }

    @Test(expected = ResetTokenInvalidException.class)
    public void resetPasswordException() {
        when(userRepository.findByResetKey(RESET_KEY)).thenThrow(
                new ResetTokenInvalidException("Reset token invalid"));
        TokenPasswordJson tokenPasswordJson = new TokenPasswordJson(RESET_KEY, PASSWORD);
        userService.resetPassword(tokenPasswordJson);
    }
}
