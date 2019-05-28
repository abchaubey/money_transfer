package com.revoult.money.transfer.dao;

import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.error.AccountExistException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountDaoTest {

    private AccountDao accountDao = AccountDao.getInstance();

    @BeforeEach
    void cleanUp() {
        accountDao.removeAll();
    }

    @Test
    public void addAccount() {
        accountDao.removeAll();
        Account anyAccount = new Account(1l, "abhay", BigDecimal.valueOf(102.10));

        accountDao.addAccount(anyAccount);

        assertEquals(1, accountDao.getAll().size());
        assertEquals(anyAccount, accountDao.getByAccountNumber(anyAccount.getAccountNumber()));
    }

    @Test
    public void accountExistForDuplicateAccounts() {
        accountDao.removeAll();
        Account account = new Account(1l, "abhay", BigDecimal.valueOf(102.10));

        accountDao.addAccount(account);

        Account accountWithExistingId = new Account(1l, "ab4hay", BigDecimal.valueOf(104.10));

        assertThrows(AccountExistException.class, () -> accountDao.addAccount(accountWithExistingId));
        assertEquals(1, accountDao.getAll().size());
    }

    @Test
    public void removeAllAccounts() {
        accountDao.removeAll();
        LongStream.range(0, 3).forEach(a -> accountDao.addAccount(new Account(a, "1" + a, BigDecimal.TEN)));

        assertEquals(3, accountDao.getAll().size());

        accountDao.removeAll();

        assertEquals(0, accountDao.getAll().size());
    }

    @Test
    public void getExistingAccount() {
        Account account = new Account(11l, "abhay", BigDecimal.valueOf(102.10));

        accountDao.addAccount(account);

        Account retrievedAccount = accountDao.getByAccountNumber(11l);

        assertEquals(account, retrievedAccount);
    }


}