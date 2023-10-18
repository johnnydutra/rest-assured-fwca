package core;

import io.restassured.http.ContentType;

public interface Constants {

    String APP_BASE_URL = "https://barrigarest.wcaquino.me";
    Integer APP_PORT = 443;
    String APP_BASE_PATH = "";
    ContentType APP_CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT = 5000L;

    String SEU_BARRIGA_USERNAME = "johnnysdet";
    String SEU_BARRIGA_EMAIL = "sdetjohnny@gmail.com";
    String SEU_BARRIGA_PASSWORD = "teste54321";
}
