package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AuthTest {

    public final String SEU_BARRIGA_USERNAME = "johnnysdet";
    public final String SEU_BARRIGA_EMAIL = "sdetjohnny@gmail.com";
    public final String SEU_BARRIGA_PASSWORD = "teste54321";


    @Test
    public void shouldAccessStarWarsAPI() {
        given()
            .log().all()
        .when()
            .get("https://swapi.dev/api/people/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Luke Skywalker"))
        ;
    }

    @Test
    public void shouldGetWeather() {
        given()
            .log().all()
            .queryParam("Fortaleza,BR")
            .queryParam("appid", "f405a9d896df7f5ad58dae514a38b6f1")
            .queryParam("units", "metric")
        .when()
            .get("https://api.openweathermap.org/data/2.5/weather")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Fortaleza"))
            .body("coord.lon", is(-38.52f))
            .body("coord.lat", is(-3.73f))
            .body("main.temp", greaterThan(18))
        ;
    }

    @Test
    public void shouldNotAccessWithoutPassword() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(401)
        ;
    }

    @Test
    public void shouldPerformBasicAuth() {
        given()
            .log().all()
        .when()
            .get("https://admin:senha@restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(200)
            .body("status", is("logado"))
        ;
    }

    @Test
    public void shouldPerformBasicAuth2() {
        given()
            .log().all()
            .auth().basic("admin", "senha")
        .when()
            .get("https://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(200)
            .body("status", is("logado"))
        ;
    }

    @Test
    public void shouldPerformBasicAuthWithChallenge() {
        given()
            .log().all()
            .auth().preemptive().basic("admin", "senha")
        .when()
            .get("https://restapi.wcaquino.me/basicauth2")
        .then()
            .log().all()
            .statusCode(200)
            .body("status", is("logado"))
        ;
    }

    @Test
    public void shouldAuthenticateWithToken() {
        Map<String, String> credentials = new HashMap<String, String>();
        credentials.put("email", SEU_BARRIGA_EMAIL);
        credentials.put("senha", SEU_BARRIGA_PASSWORD);

        String token =
        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(credentials)
        .when()
            .post("https://barrigarest.wcaquino.me/signin")
        .then()
            .log().all()
            .statusCode(200)
            .extract().path("token")
        ;

        given()
            .log().all()
            .header("Authorization", "JWT " + token)
        .when()
            .get("https://barrigarest.wcaquino.me/contas")
        .then()
            .log().all()
            .statusCode(200)
            .body(hasItem("Test"))
        ;
    }

    @Test
    public void shouldAccessWebApplication() {

        String cookie =
        given()
            .log().all()
            .formParam("email", SEU_BARRIGA_EMAIL)
            .formParam("senha", SEU_BARRIGA_PASSWORD)
            .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .when()
            .post("https://seubarriga.wcaquino.me/logar")
        .then()
            .log().all()
            .statusCode(200)
            .extract().header("set-cookie")
        ;
        cookie = cookie.split("=")[1].split(";")[0];

        String body =
        given()
            .log().all()
            .cookie("connect.sid", cookie)
        .when()
            .get("https://seubarriga.wcaquino.me/contas")
        .then()
            .log().all()
            .statusCode(200)
            .body("html.body.table.tbody.tr[0].td[0]", is("Test"))
            .extract().body().asString();
        ;

        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
        Assert.assertTrue(xmlPath.getString("html.body.table.tbody.tr[0].td[0]").equalsIgnoreCase("Test"));
    }
}
