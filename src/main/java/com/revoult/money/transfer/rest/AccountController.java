package com.revoult.money.transfer.rest;

import com.revoult.money.transfer.dao.AccountDao;
import com.revoult.money.transfer.dao.TransactionDao;
import com.revoult.money.transfer.domain.Account;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collections;


@Path("/accounts")
@RequestScoped
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final AccountDao accountDao = AccountDao.getInstance();

    private final TransactionDao transactionDao = TransactionDao.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        LOG.info("size of getAllAccounts ", accountDao.getAll().size());
        return Response.ok(Collections.unmodifiableCollection(accountDao.getAll())).build();
    }

    @GET
    @Path("{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountByAccountNumber(@PathParam("accountNumber") Long accountNumber) {
        Account account = accountDao.getByAccountNumber(accountNumber);
        LOG.info("Result for getAccountByAccountNumber", account.toString());
        return Response.ok(account).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAccount(Account account) {
        accountDao.addAccount(account);
        LOG.info("Result for createNewAccount", account.toString());
        return Response.ok(account).build();
    }

    @GET
    @Path("/{accountNumber}/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@PathParam("accountNumber") Long accountId) {

        final Account account = accountDao.getByAccountNumber(accountId);
        LOG.info("Result for getBalance", account.toString());
        return Response.ok(account).build();
    }


    @PUT
    @Path("/{accountNumber}/deposit/{amount}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(@PathParam("accountNumber") Long accountId, @PathParam("amount") BigDecimal amount) {

        Account account = transactionDao.addToAccount(accountId, amount);
        LOG.info("Result for deposit", account.toString());
        return Response.ok(account).build();
    }

    @PUT
    @Path("/{accountNumber}/withdraw/{amount}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(@PathParam("accountNumber") long accountId, @PathParam("amount") BigDecimal amount) {

        Account account = transactionDao.withDrawFromAccount(accountId, amount);
        return Response.ok(account).build();
    }

    @DELETE
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("accountId") long accountId) {

        accountDao.deleteAccount(accountId);
        LOG.info("Result for deleteAccount", accountId);
        return Response.ok("Account Deleted with Account number " + accountId).build();
    }

}
