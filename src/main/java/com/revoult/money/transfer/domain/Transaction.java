package com.revoult.money.transfer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {
    @JsonProperty(required = true)
    private BigDecimal amount;

    @JsonProperty(required = true)
    private Long fromAccountNumber;

    @JsonProperty(required = true)
    private Long toAccountNumber;

    public Transaction(BigDecimal amount, Long fromAccountNumber, Long toAccountNumber) {
        this.amount = amount;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
    }

    public Transaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(fromAccountNumber, that.fromAccountNumber) &&
                Objects.equals(toAccountNumber, that.toAccountNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, fromAccountNumber, toAccountNumber);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(Long fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public Long getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(Long toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
}
