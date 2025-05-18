package com.syemon.invoicemanagement.domain.repository;

import com.syemon.invoicemanagement.domain.Owner;

import java.util.Optional;

public interface OwnerRepository {

    Optional<Owner> findByUsername(String username);
}
