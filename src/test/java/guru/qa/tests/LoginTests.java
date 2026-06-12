package guru.qa.tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {

    String username = "Aleksandr002";
    String password = "A12345";
    String wrongPassword = "A12345_752";

    @Test
    public void successfulLoginTest(){
        LoginBodyModel data = new LoginBodyModel(username,password);

        SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String expectedTokenPart ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPart);
        assertThat(actualRefresh).startsWith(expectedTokenPart);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }


    @Test
    public void wrongCredentialsLoginTest(){
        LoginBodyModel data = new LoginBodyModel(username,wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);

        String expectedDetailError ="Invalid username or password.";
        String actualDetailError = loginResponse.detail();

        assertThat(expectedDetailError).isEqualTo(actualDetailError);

    }
/*
    @Test
    public void LoginTest(){
        LoginBodyModel data = new LoginBodyModel(username,password);

        SuccessfulLoginResponseModel loginResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .basePath("/api/v1")
                .when()
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath
                        ("schemas/login/successful_login_response_schemas.json"))
                .body("access", notNullValue())
                .body("refresh", notNullValue())
                .extract().as(SuccessfulLoginResponseModel.class);

        String expectedTokenPart ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPart);
        assertThat(actualRefresh).startsWith(expectedTokenPart);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }


    @Test
    public void wrongCredentialsLoginTest(){
        LoginBodyModel data = new LoginBodyModel(username,wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .basePath("/api/v1")
                .when()
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("schemas/login/wrong_credentials_login_response_schemas.json"))
                .body("detail", notNullValue())
                .extract().as(WrongCredentialsLoginResponseModel.class);

        String expectedDetailError ="Invalid username or password.";
        String actualDetailError = loginResponse.detail();


        assertThat(expectedDetailError).isEqualTo(actualDetailError);

    }*/
}
