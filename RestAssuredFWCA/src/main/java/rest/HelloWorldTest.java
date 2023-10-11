package rest;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HelloWorldTest {

    @Test
    public void helloWorldTest() {

        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("Status code should be 200", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validation = response.then();
        validation.statusCode(200);
    }

    @Test
    public void shouldKnowOtherRestAssuredForms() {
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validation = response.then();
        validation.statusCode(200);

        get("http://restapi.wcaquino.me/ola").then().statusCode(200);

        given()
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .statusCode(200);
    }

    @Test
    public void shouldKnowHamcrestMatchers() {
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d, isA(Double.class));
        assertThat(128d, greaterThan(120d));
        assertThat(128d, lessThan(130d));

        List<Integer> odds = Arrays.asList(1, 3, 5, 7, 9);
        assertThat(odds, hasSize(5));
        assertThat(odds, contains(1, 3, 5, 7, 9));
        assertThat(odds, containsInAnyOrder(1, 3, 5, 9, 7));
        assertThat(odds, hasItem(1));
        assertThat(odds, hasItems(1, 5));

        assertThat("Maria", is(not("João")));
        assertThat("Maria", not("João"));
        assertThat("Maria", anyOf(is("Maria"), is("Joaquina")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qu")));
    }

    @Test
    public void shouldValidateBody() {
        given()
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .statusCode(200)
            .body(is("Ola Mundo!"))
            .body(containsString("Mundo"))
            .body(is(not(nullValue())));
    }
}
