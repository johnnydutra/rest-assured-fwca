package refactor;

import core.Hooks;
import org.junit.Test;
import pojo.Account;

import static utils.Helpers.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class AccountTest extends Hooks {

    @Test
    public void shouldAddAccountSuccessfully() {

            given()
                .body(new Account("Conta inserida"))
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
        String updatedName = "Conta alterada";

        given()
            .body(new Account(updatedName))
            .pathParam("id", getAccountIdByName("Conta para alterar"))
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is(updatedName))
        ;
    }

    @Test
    public void shouldNotAddDuplicatedAccount() {

        given()
            .body(new Account("Conta mesmo nome"))
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void shouldNotDeleteAccountWithTransactions() {

        given()
            .pathParam("id", getAccountIdByName("Conta com movimentacao"))
        .when()
            .delete("/contas/{id}")
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }
}
