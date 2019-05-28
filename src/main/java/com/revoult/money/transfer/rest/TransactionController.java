package com.revoult.money.transfer.rest;

import com.revoult.money.transfer.dao.TransactionDao;
import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transactions")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionDao transactionDao = TransactionDao.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferMoney(Transaction transaction) {
        List<Account> updatedAccount = transactionDao.transferMoneyBetweenAccounts(transaction);
        LOG.info("transfer money for account ", updatedAccount.toString());
        return Response.ok(updatedAccount).build();
    }

}
