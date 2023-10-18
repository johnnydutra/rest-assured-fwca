package core;

import io.restassured.http.ContentType;
import pojo.Transaction;
import utils.DateUtils;

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

    public static Transaction getValidTransaction(Integer accountId) {
        Transaction transaction = new Transaction();
        transaction.setConta_id(accountId);
        transaction.setDescricao("Added with REST-assured");
        transaction.setEnvolvido("FWCA");
        transaction.setTipo("REC");
        transaction.setData_transacao(DateUtils.getDateWithDaysDifference(-1));
        transaction.setData_pagamento(DateUtils.getDateWithDaysDifference(5));
        transaction.setValor(123f);
        transaction.setStatus(true);
        return transaction;
    }
}
