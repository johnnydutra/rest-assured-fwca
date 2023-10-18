package core;

import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class Actions {

    public static String getToken(String email, String password) {

        Map<String, String> credentials = new HashMap<String, String>();
        credentials.put("email", email);
        credentials.put("senha", password);

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

        return token;
    }
}
