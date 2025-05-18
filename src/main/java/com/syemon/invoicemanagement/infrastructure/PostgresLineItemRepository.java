package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.LineItemRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PostgresLineItemRepository implements LineItemRepository {

    private final LineItemJpaRepository repository;
    private final LineItemInfrastructureMapper lineItemInfrastructureMapper;

    @Override
    public List<LineItem> saveAll(List<LineItem> lineItems) {
        List<LineItemJpaEntity> lineItemJpaEntities = lineItems.stream()
                .map(lineItemInfrastructureMapper::toEntity)
                .toList();
        List<LineItemJpaEntity> persistedLineItems = repository.saveAll(lineItemJpaEntities);
        return persistedLineItems.stream()
                .map(lineItemInfrastructureMapper::toDomain)
                .toList();
    }
}
