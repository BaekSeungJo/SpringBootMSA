package com.sjbaek.chapter3.domain;

import java.io.Serializable;
import java.util.Currency;
import java.util.Objects;

public final class Money implements Serializable {
    private final Long value;
    private final Currency currency;

    public Money(Long value, Currency currency) {
        if(value == null || value < 0) {
            throw new IllegalArgumentException("invalid value=" + value);
        }

        if(currency == null) {
            throw new IllegalArgumentException("invalid currency");
        }

        this.value = value;
        this.currency = currency;
    }

    public Long getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }
}
