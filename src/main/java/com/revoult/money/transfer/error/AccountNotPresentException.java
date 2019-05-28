package com.revoult.money.transfer.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNotPresentException extends RuntimeException implements ExceptionMapper<AccountNotPresentException> {

    public AccountNotPresentException(Long accountId) {
        super("Account with account Number:" + accountId + " doesn't exists.");
    }

    public AccountNotPresentException() {
        super();
    }

    @Override
    public Response toResponse(AccountNotPresentException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
