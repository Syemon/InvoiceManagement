package com.syemon.invoicemanagement.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "lineitem")
public class LineItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime createTime;
    private OffsetDateTime modifyTime;

    @Version
    private Integer version;

    private String description;
    private BigDecimal amountPerItem;
    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private Integer quantity;
    private BigDecimal tax;

    @ManyToOne
    @JoinColumn(name = "invoice")
    private InvoiceJpaEntity invoice;

    @PrePersist
    public void prePersist() {
        createTime = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifyTime = OffsetDateTime.now();
    }
}
