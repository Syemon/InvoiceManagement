package com.syemon.invoicemanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemJpaRepository extends JpaRepository<LineItemJpaEntity, Long> {

}
