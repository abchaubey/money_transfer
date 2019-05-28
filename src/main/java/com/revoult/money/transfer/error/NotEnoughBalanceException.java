package com.revoult.money.transfer.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotEnoughBalanceException extends RuntimeException implements ExceptionMapper<NotEnoughBalanceException> {

    public NotEnoughBalanceException() {
        super("Insufficient account balance to perform fund transfer.");
    }

    @Override
    public Response toResponse(NotEnoughBalanceException exception) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
