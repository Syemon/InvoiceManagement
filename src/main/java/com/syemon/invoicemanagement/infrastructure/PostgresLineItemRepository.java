package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.LineItem;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PostgresLineItemRepository implements LineItemRepository {

    private final LineItemJpaRepository repository;

    @Override
    public List<LineItemJpaEntity> saveAll(List<LineItemJpaEntity> lineItems) {
        return repository.saveAll(lineItems);
    }
}
