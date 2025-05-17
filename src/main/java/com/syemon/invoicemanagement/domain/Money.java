package com.syemon.invoicemanagement.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

@Getter
public class Money{
    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public Money add(Money other) {
        checkSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(new BigDecimal(quantity)), currency);
    }

    public Money applyVAT(BigDecimal vatRate) {
        BigDecimal multiplier = BigDecimal.ONE.add(vatRate.multiply(new BigDecimal("0.01")));
        return new Money(amount.multiply(multiplier), currency);
    }

    private void checkSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: %s vs %s".formatted(currency, other.currency));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", currency.getCurrencyCode(), amount);
    }
}
