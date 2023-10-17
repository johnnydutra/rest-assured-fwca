package rest;


import org.junit.Test;
import org.xml.sax.SAXParseException;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;

public class SchemaTest {

    @Test
    public void shouldValidateXmlSchema() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/usersXML")
        .then()
            .statusCode(200)
            .body(matchesXsdInClasspath("users.xsd"))
        ;
    }

    @Test (expected = SAXParseException.class)
    public void shouldValidateBadXmlSchema() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/invalidUsersXML")
        .then()
            .statusCode(200)
            .body(matchesXsdInClasspath("users.xsd"))
        ;
    }

    @Test
    public void shouldValidateJsonSchema() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
        ;
    }
}
