package com.revoult.money.transfer.rest;

import com.revoult.money.transfer.dao.AccountDao;
import com.revoult.money.transfer.domain.Account;
import com.revoult.money.transfer.error.AccountNotPresentException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AccountControllerTest extends JerseyTest {

    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String BALANCE = "balance";
    private static final String USER_NAME = "userName";

    private AccountDao accountDao = null;

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9998;
    }

    @Before
    public void initializeData() {
        accountDao = AccountDao.getInstance();

        accountDao.removeAll();

        accountDao.addAccount(new Account(1l, "abhay", BigDecimal.valueOf(102.10)));
        accountDao.addAccount(new Account(2l, "ayyapa", BigDecimal.valueOf(150.10)));
        accountDao.addAccount(new Account(3l, "vinay", BigDecimal.valueOf(1000.10)));
        accountDao.addAccount(new Account(4l, "pushkar", BigDecimal.valueOf(9999.10)));
    }

    @Test
    public void validCallShouldReturn_200() {
        expect().statusCode(200).contentType(ContentType.JSON)
                .when().get("/accounts/1");
    }

    @Test
    public void accountDoesNotExist() {
        expect().statusCode(500)
                .when().get("/accounts/100");
    }

    @Test
    public void getAllAccounts() {
        given().when()
                .get("/accounts")
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    public void getSpecificAccounts() {
        given().when()
                .get("/accounts/1")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(1))
                .body(USER_NAME, equalTo("abhay"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(102.10F)));
    }

    @Test
    public void addNewAccount() {
        Account newAccount = new Account(32L, "sonu", BigDecimal.valueOf(100.01));

        given().contentType("application/json").body(newAccount)
                .when()
                .post("/accounts")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(32))
                .body(USER_NAME, equalTo("sonu"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(100.01F)));
    }

    @Test
    public void withdrawFromAccount() {
        given().contentType("application/json")
                .when()
                .put("/accounts/2/withdraw/100")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(2))
                .body(USER_NAME, equalTo("ayyapa"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(50.10F)));
    }


    @Test
    public void depositToAccount() {
        given().contentType("application/json")
                .when()
                .put("/accounts/3/deposit/1")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(3))
                .body(USER_NAME, equalTo("vinay"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(1001.1F)));
    }

    @Override
    public Application configure() {
        return new ResourceConfig(AccountController.class);
    }
}