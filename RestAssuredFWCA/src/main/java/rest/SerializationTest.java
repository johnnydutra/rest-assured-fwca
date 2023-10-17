package rest;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SerializationTest {

    @Test
    public void shouldSaveUserUsingMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Map user");
        params.put("age", 25);

        given()
            .log().all()
            .contentType("application/json")
            .body(params)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is(params.get("name")))
            .body("age", is(params.get("age")))
        ;
    }

    @Test
    public void shouldSaveUserUsingObject() {
        User user = new User("Object user", 35);

        given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is(user.getName()))
            .body("age", is(user.getAge()))
        ;
    }

    @Test
    public void shouldDeserializeObjectAfterSavingUser() {
        User user = new User("Object user", 35);

        User savedUser =
        given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
        ;

        Assert.assertThat(savedUser.getId(), notNullValue());
        Assert.assertEquals(user.getName(), savedUser.getName());
        Assert.assertThat(savedUser.getAge(), is(user.getAge()));
        System.out.println(savedUser);
    }

    @Test
    public void shouldSaveUserWithXmlUsingObject() {
        User user = new User("XML user", 40);

        given()
            .log().all()
            .contentType(ContentType.XML)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is(user.getName()))
            .body("user.age", is(user.getAge().toString()))
        ;
    }

    @Test
    public void shouldDeserializeSavedUserWithXml() {
        User user = new User("XML user", 40);

        User savedUser =
        given()
            .log().all()
            .contentType(ContentType.XML)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
        ;

        Assert.assertThat(savedUser.getId(), notNullValue());
        Assert.assertEquals(user.getName(), savedUser.getName());
        Assert.assertThat(savedUser.getAge(), is(user.getAge()));
        System.out.println(savedUser);
    }
}
