package refactor;

import utils.Helpers;
import core.Hooks;
import org.junit.Test;
import pojo.Transaction;
import utils.DateUtils;

import static utils.Helpers.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TransactionTest extends Hooks {

    @Test
    public void shouldAddTransactionSuccessfully() {
        Transaction newTransaction = Helpers.getValidTransaction(getAccountIdByName("Conta para movimentacoes"));

        given()
            .body(newTransaction)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(201)
        ;
    }

    @Test
    public void shouldValidateMandatoryTransactionFields() {

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
    public void shouldNotAddFutureTransaction() {
        Transaction futureTransaction = Helpers.getValidTransaction(getAccountIdByName("Conta para movimentacoes"));
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
    public void shouldDeleteTransaction() {

        given()
            .pathParam("id", getTransactionIdByDescription("Movimentacao para exclusao"))
        .when()
            .delete("/transacoes/{id}")
        .then()
            .statusCode(204)
        ;
    }
}
