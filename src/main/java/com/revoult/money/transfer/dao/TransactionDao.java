package com.revoult.money.transfer.dao;

import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.domain.Transaction;
import com.revoult.money.transfer.error.AccountNotPresentException;
import com.revoult.money.transfer.error.InvalidAmountException;
import com.revoult.money.transfer.error.NotEnoughBalanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionDao {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionDao.class);
    private AccountDao accountDao = AccountDao.getInstance();


    public TransactionDao() {
    }

    public static TransactionDao getInstance() {
        return Instance.instance;
    }

    public List<Account> transferMoneyBetweenAccounts(Transaction transaction) {

        List<Account> accounts = new ArrayList<>();
        Account sourceAccount = accountDao.getByAccountNumber(transaction.getFromAccountNumber());
        Account targetAccount = accountDao.getByAccountNumber(transaction.getToAccountNumber());

        if (sourceAccount == null || targetAccount == null) {
            LOG.warn("Account not present");
            Account account = sourceAccount != null ? sourceAccount : targetAccount;
            throw new AccountNotPresentException(account.getAccountNumber());
        }

        BigDecimal amount = transaction.getAmount();

        if (sourceAccount.getLock().tryLock()) {
            try {
                if (targetAccount.getLock().tryLock()) {
                    try {
                        if (amount.compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)) <= 0) {
                            LOG.warn("Invalid amount to be transferred");
                            throw new InvalidAmountException();
                        }

                        if (sourceAccount.getBalance().compareTo(amount) < 0) {
                            LOG.warn("Not enough balance in source account to be transferred");
                            throw new NotEnoughBalanceException();
                        }

                        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
                        targetAccount.setBalance(targetAccount.getBalance().add(amount));

                        accounts.add(sourceAccount);
                        accounts.add(targetAccount);

                    } finally {
                        targetAccount.getLock().unlock();
                    }
                }
            } finally {
                sourceAccount.getLock().unlock();
            }
        }

        return accounts;
    }

    public Account addToAccount(Long accountNumber, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)) <= 0) {
            LOG.warn("Invalid amount");
            throw new InvalidAmountException();
        }

        final Account account = accountDao.getByAccountNumber(accountNumber);
        if (account.getLock().tryLock()) {
            try {
                account.setBalance(account.getBalance().add(amount));
            } finally {
                account.getLock().unlock();
            }
        }
        return account;
    }

    public Account withDrawFromAccount(Long accountNumber, BigDecimal amount) {

        if (amount.compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)) <= 0) {
            LOG.warn("Invalid amount");
            throw new InvalidAmountException();
        }

        final Account account = accountDao.getByAccountNumber(accountNumber);

        if (account.getLock().tryLock()) {
            try {
                account.setBalance(account.getBalance().subtract(amount));
            } finally {
                account.getLock().unlock();
            }
        }

        return account;
    }

    private static class Instance {
        private static final TransactionDao instance = new TransactionDao();
    }
}
