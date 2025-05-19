package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.create.LineItemModel;
import com.syemon.invoicemanagement.infrastructure.LineItemJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
public interface LineItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "totalTaxAmount", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "amountPerItem", source = "amountPerItem")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "tax", source = "tax")
    LineItemJpaEntity toEntity(LineItemModel model);
}