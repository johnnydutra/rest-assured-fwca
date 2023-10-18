package tests;

import core.Actions;
import core.Hooks;

import org.junit.Test;
import pojo.Account;
import pojo.Transaction;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends Hooks {

    String ACCESS_TOKEN = Actions.getToken(SEU_BARRIGA_EMAIL, SEU_BARRIGA_PASSWORD);
    Account newAccount = new Account("New Account");
    Account accountUpdate = new Account("Updated Account");
    Account existingAccount = new Account("Existing Account");
    Integer accountUpdateId = 1946993;
    Integer accountWithTransactionsId = 1946998;

    @Test
    public void shouldNotAccessWithoutToken() {
        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401)
        ;
    }

    @Test
    public void shouldAddAccountSuccessfully() {

        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
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
    public void shouldUpdateAccountSuccessfully() {
        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
            .body(accountUpdate)
        .when()
            .put("/contas/" + accountUpdateId)
        .then()
            .statusCode(200)
            .body("nome", is(accountUpdate.getNome()));
    }

    @Test
    public void shouldNotAddDuplicatedAccount() {
        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
            .body(existingAccount)
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void shouldAddTransactionSuccessfully() {
        Transaction newTransaction = new Transaction();
        newTransaction.setConta_id(1946998);
        newTransaction.setDescricao("Added with REST-assured");
        newTransaction.setEnvolvido("FWCA");
        newTransaction.setTipo("REC");
        newTransaction.setData_transacao("01/01/2023");
        newTransaction.setData_pagamento("30/03/2023");
        newTransaction.setValor(123f);
        newTransaction.setStatus(true);

        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
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
            .header("Authorization", "JWT " + ACCESS_TOKEN)
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
        Transaction futureTransaction = new Transaction();
        futureTransaction.setConta_id(1946998);
        futureTransaction.setDescricao("Future Transaction");
        futureTransaction.setEnvolvido("FWCA");
        futureTransaction.setTipo("REC");
        futureTransaction.setData_transacao("01/01/2024");
        futureTransaction.setData_pagamento("30/03/2024");
        futureTransaction.setValor(123f);
        futureTransaction.setStatus(true);

        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
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
    public void shouldNotDeleteAccountWithTransactions() {
        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
        .when()
            .delete("/contas/" + accountWithTransactionsId)
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void shouldCalculateAccountBalance() {
        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
        .when()
            .get("/saldo")
        .then()
            .statusCode(200)
            .body("find { it.conta_id == 1946998 }.saldo", is("123.00"))
        ;
    }

    @Test
    public void shouldDeleteTransaction() {
        given()
            .header("Authorization", "JWT " + ACCESS_TOKEN)
        .when()
            .delete("/transacoes/1825175")
        .then()
            .statusCode(204)
        ;
    }
}