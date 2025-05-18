package com.syemon.invoicemanagement.domain;

import java.util.List;

public interface LineItemRepository {
    List<LineItem> saveAll(List<LineItem> lineItems);
}
