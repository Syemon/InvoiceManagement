package com.syemon.invoicemanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerJpaRepository extends JpaRepository<OwnerJpaEntity, Long> {

    Optional<OwnerJpaEntity> findByUsername(String username);

}
