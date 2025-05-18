package com.syemon.invoicemanagement.domain;

import java.util.Optional;

public interface OwnerRepository {

    Optional<Owner> findByUsername(String username);
}
