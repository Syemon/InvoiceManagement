package com.syemon.invoicemanagement.infrastructure;

import java.util.List;

public interface LineItemRepository {
    List<LineItemJpaEntity> saveAll(List<LineItemJpaEntity> lineItems);
}
