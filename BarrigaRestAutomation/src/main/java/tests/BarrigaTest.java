package tests;

import utils.Helpers;
import core.Hooks;

import io.restassured.specification.FilterableRequestSpecification;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import pojo.Account;
import pojo.Transaction;
import utils.DateUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends Hooks {

    private static final String newAccountName = "Account " + System.nanoTime();
    private static Integer newAccountId;
    private static Integer newTransactionId;


    Account newAccount = new Account(newAccountName);
    Account accountUpdate = new Account("Updated " + newAccountName);


    @BeforeClass
    public static void login() {
        String ACCESS_TOKEN = Helpers.getToken(SEU_BARRIGA_EMAIL, SEU_BARRIGA_PASSWORD);
        requestSpecification.header("Authorization", "JWT " + ACCESS_TOKEN);
    }

    @Test
    public void t01_shouldAddAccountSuccessfully() {

        newAccountId =
            given()
                .body(newAccount)
            .when()
                .post("/contas")
            .then()
                .log().all()
                .statusCode(201)
                .extract().path("id")
            ;
    }

    @Test
    public void t02_shouldUpdateAccountSuccessfully() {

        given()
            .body(accountUpdate)
            .pathParam("id", newAccountId)
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is(accountUpdate.getNome()));
    }

    @Test
    public void t03_shouldNotAddDuplicatedAccount() {

        given()
            .body(accountUpdate)
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void t04_shouldAddTransactionSuccessfully() {
        Transaction newTransaction = Helpers.getValidTransaction(newAccountId);

        newTransactionId =
            given()
                .body(newTransaction)
            .when()
                .post("/transacoes")
            .then()
                .statusCode(201)
                .extract().path("id")
            ;
    }

    @Test
    public void t05_shouldValidateMandatoryTransactionFields() {

        given()
            .body("{}")
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(8))
            .body("msg", hasItems(
                    "Data da Movimentação é obrigatório",
                    "Data do pagamento é obrigatório",
                    "Descrição é obrigatório",
                    "Interessado é obrigatório",
                    "Valor é obrigatório",
                    "Valor deve ser um número",
                    "Conta é obrigatório",
                    "Situação é obrigatório"
            ))
        ;
    }

    @Test
    public void t06_shouldNotAddFutureTransaction() {
        Transaction futureTransaction = Helpers.getValidTransaction(newAccountId);
        futureTransaction.setData_transacao(DateUtils.getDateWithDaysDifference(2));
        futureTransaction.setData_pagamento(DateUtils.getDateWithDaysDifference(5));

        given()
            .body(futureTransaction)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(1))
            .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void t07_shouldNotDeleteAccountWithTransactions() {

        given()
            .pathParam("id", newAccountId)
        .when()
            .delete("/contas/{id}")
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void t08_shouldCalculateAccountBalance() {

        given()
        .when()
            .get("/saldo")
        .then()
            .statusCode(200)
            .body("find { it.conta_id == " + newAccountId + " }.saldo", is("123.00"))
        ;
    }

    @Test
    public void t09_shouldDeleteTransaction() {

        given()
            .pathParam("id", newTransactionId)
        .when()
            .delete("/transacoes/{id}")
        .then()
            .statusCode(204)
        ;
    }

    @Test
    public void t10_shouldNotAccessWithoutToken() {
        FilterableRequestSpecification request = (FilterableRequestSpecification) requestSpecification;
        request.removeHeader("Authorization");

        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401)
        ;
    }
}