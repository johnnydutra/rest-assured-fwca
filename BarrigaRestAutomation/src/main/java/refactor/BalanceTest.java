package refactor;

import core.Hooks;
import org.junit.Test;

import static utils.Helpers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class BalanceTest extends Hooks {

    @Test
    public void shouldCalculateAccountBalance() {

        given()
            .when()
        .get("/saldo")
        .then()
            .statusCode(200)
            .body("find { it.conta_id == " + getAccountIdByName("Conta para saldo") + " }.saldo", is("534.00"))
        ;
    }
}
