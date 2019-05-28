package com.revoult.money.transfer.rest;

import com.revoult.money.transfer.dao.AccountDao;
import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.domain.Transaction;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TransactionControllerTest extends JerseyTest {

    private static final String FROM_ACCOUNT_NUMBER = "fromAccountNumber";
    private static final String TO_ACCOUNT_NUMBER = "toAccountNumber";
    private static final String AMOUNT = "amount";

    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String BALANCE = "balance";
    private static final String USER_NAME = "userName";

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9998;
    }

    @Before
    public void initializeData() {

        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    public void validTransactionShouldSucceed() {

        AccountDao accountDao = AccountDao.getInstance();
        accountDao.addAccount(new Account(14l, "abhay", BigDecimal.valueOf(102.10)));
        accountDao.addAccount(new Account(22l, "ayyapa", BigDecimal.valueOf(150.10)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(100), 14l, 22l);
        given().contentType("application/json").body(transaction)
                .when()
                .post("/transactions")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(Arrays.asList(14,22)))
                .body(USER_NAME, equalTo(Arrays.asList("abhay", "ayyapa")));
    }

    @Override
    public javax.ws.rs.core.Application configure() {
        return new ResourceConfig(TransactionController.class);
    }

}