package com.syemon.invoicemanagement.domain.repository;

import com.syemon.invoicemanagement.domain.LineItem;

import java.util.List;

public interface LineItemRepository {
    List<LineItem> saveAll(List<LineItem> lineItems);
}
