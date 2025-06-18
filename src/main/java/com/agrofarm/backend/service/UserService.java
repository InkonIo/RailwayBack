package com.agrofarm.backend.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agrofarm.backend.entity.User;
import com.agrofarm.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Лучше внедрять через @Bean
    }

    @Transactional
    public User registerUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role("USER") // Можно вынести в enum
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User loginUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!checkPassword(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }
}
