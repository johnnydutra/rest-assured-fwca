package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.Test;

public class VerbsTest {

    @Test
    public void shouldSaveUser() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Johnny\", \"age\": 32 }")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Johnny"))
            .body("age", is(32))
        ;
    }

    @Test
    public void shouldNotSaveNamelessUser() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"age\": 32 }")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(400)
            .body("id", is(nullValue()))
            .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void shouldSaveUserWithXml() {
        given()
            .log().all()
            .contentType(ContentType.XML)
            .body("<user> <name>Johnny</name> <age>32</age> </user>")
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is("Johnny"))
            .body("user.age", is("32"))
        ;
    }

    @Test
    public void shouldUpdateUser() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Updated user\", \"age\": 80 }")
        .when()
            .put("https://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Updated user"))
            .body("age", is(80))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void shouldCustomizeUrl() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Updated user\", \"age\": 80 }")
        .when()
            .put("https://restapi.wcaquino.me/{entity}/{userId}", "users", "1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Updated user"))
            .body("age", is(80))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void shouldCustomizeUrlPart2() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Updated user\", \"age\": 80 }")
            .pathParam("entity", "users")
            .pathParam("userId", 1)
        .when()
            .put("https://restapi.wcaquino.me/{entity}/{userId}")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Updated user"))
            .body("age", is(80))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void shouldRemoveUser() {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(204)
        ;
    }

    @Test
    public void shouldNotRemoveNonexistentUser() {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/1000")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", is("Registro inexistente"))
        ;
    }
}
