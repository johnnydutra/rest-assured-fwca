package refactor;

import core.Hooks;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class AuthenticationTest extends Hooks {

    @Test
    public void shouldNotAccessWithoutToken() {
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
