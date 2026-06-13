package guru.qa.tests;

import io.restassured.http.ContentType;
import models.login.RefreshBodyModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static specs.login.LoginSpec.refreshRequestSpec;
import static specs.login.LoginSpec.successfulRefreshResponseSpec;

public class LogoutTests extends TestBase {

    String username = "Aleksandr002";
    String password = "A12345";

    @Test
    public void successfulLogoutTest() {
        RefreshBodyModel data = new RefreshBodyModel(username, password);

        String refreshToken = given(refreshRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulRefreshResponseSpec)
                .extract().path("refresh");


       // String logoutData = "{\"refresh\": \"" + refreshToken + "\"}";
        String logoutData =format("{\"refresh\": \" %s \"}", refreshToken);
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(logoutData)
                .basePath("/api/v1")
                .when()
                .post("/auth/logout/")
                .then()
                .log().all()
                .statusCode(200);


    }
}
