package com.revoult.money.transfer.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountExistException extends RuntimeException implements ExceptionMapper<AccountExistException> {

    public AccountExistException(Long accountId) {
        super("Account with account Number:" + accountId + " already exists.");
    }

    public AccountExistException() {
        super();
    }

    @Override
    public Response toResponse(AccountExistException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
