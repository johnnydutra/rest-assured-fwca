package rest;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DataSendTest {

    @Test
    public void shouldSendValueViaQuery() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/v2/users?format=xml")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
        ;
    }

    @Test
    public void shouldSendValueViaQueryViaParam() {
        given()
            .log().all()
            .queryParam("format", "json")
            .queryParam("another", "param")
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void shouldSendValueViaHeader() {
        given()
            .log().all()
            .accept(ContentType.XML)
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
        ;
    }
}
