package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Owner;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerPostgresRepository implements OwnerRepository {

    private final OwnerJpaRepository ownerJpaRepository;

    public Optional<OwnerJpaEntity> findByUsername(String username) {
        return ownerJpaRepository.findByUsername(username);

    }
}