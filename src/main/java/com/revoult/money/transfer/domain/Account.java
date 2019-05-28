package com.revoult.money.transfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    @JsonProperty(required = true)
    private Long accountNumber;

    @JsonProperty(required = true)
    private String userName;

    @JsonProperty(required = true)
    private BigDecimal balance;

    @JsonIgnore
    private Lock lock = new ReentrantLock();

    public Account() {
    }

    public Account(Long accountId, String userName, BigDecimal balance) {
        this.accountNumber = accountId;
        this.userName = userName;
        this.balance = balance;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(userName, account.userName) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accountNumber, userName, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", userName='" + userName + '\'' +
                ", balance=" + balance +
                '}';
    }

    public Lock getLock() {
        return lock;
    }
}