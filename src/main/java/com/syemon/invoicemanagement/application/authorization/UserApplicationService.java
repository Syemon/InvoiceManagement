package com.syemon.invoicemanagement.application.authorization;

import com.syemon.invoicemanagement.domain.User;
import com.syemon.invoicemanagement.infrastructure.UserJpaEntity;
import com.syemon.invoicemanagement.infrastructure.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserApplicationService {
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<UserJpaEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<UserJpaEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Optional<UserJpaEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public UserJpaEntity createUser(UserJpaEntity user) {
        if (userRepository.findByUsername(user.getLogin()).isPresent()) {
            throw new RuntimeException("Username already exists: " + user.getLogin());
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
