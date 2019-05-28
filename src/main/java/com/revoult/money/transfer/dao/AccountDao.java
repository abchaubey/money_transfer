package com.revoult.money.transfer.dao;

import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.error.AccountExistException;
import com.revoult.money.transfer.error.AccountNotPresentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountDao {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDao.class);

    private final ConcurrentMap<Long, Account> accounts;

    public AccountDao() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public static AccountDao getInstance() {
        return Instance.instance;
    }

    public Account getByAccountNumber(Long accountNumber) {
        final Account account = accounts.get(accountNumber);
        if (account == null) {
            LOG.warn("Account not Present");
            throw new AccountNotPresentException(accountNumber);
        }
        return account;
    }

    public Collection<Account> getAll() {
        return accounts.values();
    }

    public Account addAccount(Account account) {
        Account accountExists = accounts.putIfAbsent(account.getAccountNumber(), account);

        if (accountExists != null) {
            LOG.warn("Account already Present");
            throw new AccountExistException(accountExists.getAccountNumber());
        }
        return getByAccountNumber(account.getAccountNumber());
    }

    public void removeAll() {
        accounts.clear();
    }

    public void deleteAccount(Long accountNumber) {
        final Account account = accounts.get(accountNumber);

        if (account == null) {
            LOG.warn("Account already Present");
            throw new AccountNotPresentException(accountNumber);
        }

        accounts.remove(accountNumber);
    }

    private static class Instance {
        private static final AccountDao instance = new AccountDao();
    }
}