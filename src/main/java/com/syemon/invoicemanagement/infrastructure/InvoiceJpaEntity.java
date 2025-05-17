package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.DocumentType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoice")
@Data
public class InvoiceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private OffsetDateTime createTime;
    private OffsetDateTime modifyTime;

    @Version
    private Integer version;

    @Enumerated(EnumType.STRING)
    private DocumentType invoiceHeader;

    private OffsetDateTime invoiceDate;
    private OffsetDateTime dueTime;
    private String sellerName;
    private String sellerPhoneNumber;
    private String sellerEmail;
    private String sellerAddressStreet;
    private String sellerAddressCity;
    private String sellerAddressZipCode;
    private String sellerAddressCountry;
    private String buyerName;
    private String buyerPhoneNumber;
    private String buyerEmail;
    private String buyerAddressStreet;
    private String buyerAddressCity;
    private String buyerAddressZipCode;
    private String buyerAddressCountry;

    @OneToMany(mappedBy = "invoice")
    private List<LineItemJpaEntity> lineItems;

    private BigDecimal totalAmount;

    private BigDecimal totalTaxAmount;

    private String paymentLink;

    private Currency currency;

    private boolean paid = false;

    @PrePersist
    public void prePersist() {
        createTime = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifyTime = OffsetDateTime.now();
    }
}
