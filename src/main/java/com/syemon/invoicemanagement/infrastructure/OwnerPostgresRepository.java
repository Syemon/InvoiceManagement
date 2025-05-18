package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Owner;
import com.syemon.invoicemanagement.domain.repository.OwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerPostgresRepository implements OwnerRepository {

    private final OwnerJpaRepository ownerJpaRepository;

    @Override
    public Optional<Owner> findByUsername(String username) {
        return ownerJpaRepository.findByUsername(username)
                .map(ownerJpaEntity -> new Owner(ownerJpaEntity.getUsername(), ownerJpaEntity.getPassword()));
    }
}