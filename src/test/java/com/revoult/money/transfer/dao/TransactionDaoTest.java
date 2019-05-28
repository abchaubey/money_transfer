package com.revoult.money.transfer.dao;

import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.domain.Transaction;
import com.revoult.money.transfer.error.AccountNotPresentException;
import com.revoult.money.transfer.error.InvalidAmountException;
import com.revoult.money.transfer.error.NotEnoughBalanceException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionDaoTest {

    TransactionDao transactionDao = TransactionDao.getInstance();


    @Test
    public void successfulTransaction() {
        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(1l, "abhay", BigDecimal.valueOf(102.10)));
        accountDao.addAccount(new Account(2l, "ayyapa", BigDecimal.valueOf(150.10)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(100), 1l, 2l);

        List<Account> account = transactionDao.transferMoneyBetweenAccounts(transaction);
        assertEquals(250.10, account.get(1).getBalance().doubleValue());

    }

    @Test
    public void accountNotPresentTransaction() {
        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(3l, "ayyapa", BigDecimal.valueOf(150.10)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(100), 10l, 2l);

        //Account account =
        assertThrows(AccountNotPresentException.class, () -> transactionDao.transferMoneyBetweenAccounts(transaction));


    }

    @Test
    public void InSufficientAmountTransaction() {
        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(7l, "abhay", BigDecimal.valueOf(102.10)));
        accountDao.addAccount(new Account(8l, "ayyapa", BigDecimal.valueOf(150.10)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(200), 7l, 8l);
        assertThrows(NotEnoughBalanceException.class, () -> transactionDao.transferMoneyBetweenAccounts(transaction));
    }

    @Test
    public void negativeAmountTransaction() {

        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(11l, "abhay", BigDecimal.valueOf(102.10)));
        accountDao.addAccount(new Account(12l, "ayyapa", BigDecimal.valueOf(150.10)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(-200), 11l, 12l);
        assertThrows(InvalidAmountException.class, () -> transactionDao.transferMoneyBetweenAccounts(transaction));
    }


    @Test
    public void addToAccount() {
        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(34l, "abhay", BigDecimal.valueOf(102.10)));

        Account account = transactionDao.addToAccount(34l, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(202.10), account.getBalance());
    }

    @Test
    public void withDrawFromAccount() {
        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(35l, "abhay", BigDecimal.valueOf(302.10)));

        Account account = transactionDao.withDrawFromAccount(35l, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(202.10), account.getBalance());

    }
}