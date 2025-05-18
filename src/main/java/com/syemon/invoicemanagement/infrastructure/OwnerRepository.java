package com.syemon.invoicemanagement.infrastructure;

import java.util.Optional;

public interface OwnerRepository {

    Optional<OwnerJpaEntity> findByUsername(String username);
}
