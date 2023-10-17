package rest;


import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FileTest {

    @Test
    public void shouldRequireFileSend() {
        given()
            .log().all()
        .when()
            .post("https://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(404)
            .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void shouldUploadFile() {
        given()
            .log().all()
            .multiPart("arquivo", new File("src/main/resources/test.txt"))
        .when()
            .post("https://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("test.txt"))
        ;
    }

    @Test
    public void shouldUploadLargeFile() {
        given()
            .log().all()
            .multiPart("arquivo", new File("src/main/resources/testUpload.webm"))
        .when()
            .post("https://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .time(lessThan(2000L))
            .statusCode(413)
        ;
    }

    @Test
    public void shouldDownloadFile() throws IOException {

        byte[] download =
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/download")
        .then()
            .log().all()
            .statusCode(200)
            .extract().asByteArray();
        ;

        File image = new File("src/main/resources/file.jpg");
        OutputStream output = new FileOutputStream(image);
        output.write(download);
        output.close();

        System.out.println(image.length());
        Assert.assertThat(image.length(), lessThan(100000L));
    }
}
