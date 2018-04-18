package com.aurea.boot.autoconfigure.api.user.impl;

import com.aurea.boot.autoconfigure.api.error.ResetTokenInvalidException;
import com.aurea.boot.autoconfigure.api.user.UserService;
import com.aurea.boot.autoconfigure.api.user.json.TokenPasswordJson;
import com.aurea.boot.autoconfigure.data.user.User;
import com.aurea.boot.autoconfigure.data.user.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void resetPassword(TokenPasswordJson tokenPasswordJson) {
        User user = userRepository
                .findByResetKey(tokenPasswordJson.getToken())
                .orElseThrow(() -> new ResetTokenInvalidException("Reset token invalid"));
        user.setPassword(passwordEncoder.encode(tokenPasswordJson.getPassword()));
        user.setResetKey(null);
        userRepository.save(user);
    }
}
