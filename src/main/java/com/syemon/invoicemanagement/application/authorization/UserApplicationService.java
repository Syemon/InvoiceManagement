package com.syemon.invoicemanagement.application.authorization;

import com.syemon.invoicemanagement.infrastructure.OwnerJpaEntity;
import com.syemon.invoicemanagement.infrastructure.OwnerJpaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service//TODO: for now it is more of an util class
@AllArgsConstructor
public class UserApplicationService {
    private final OwnerJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<OwnerJpaEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<OwnerJpaEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Optional<OwnerJpaEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public OwnerJpaEntity createUser(OwnerJpaEntity user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
