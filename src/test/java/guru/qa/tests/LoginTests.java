package guru.qa.tests;

import models.login.*;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.Test;
import testData.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;

public class LoginTests extends TestBase {

    TestData td = new TestData();

    @Test
    public void successfulLoginTest() {

        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(td.username, td.password);

        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel data = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String expectedTokenPart = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPart);
        assertThat(actualRefresh).startsWith(expectedTokenPart);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel data = new LoginBodyModel(td.username, td.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(invalidCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);

        String actualErrorInvalidUsernameOrPassword = loginResponse.detail();
        assertThat(actualErrorInvalidUsernameOrPassword).isEqualTo(td.expectedErrorInvalidUsernameOrPassword);

    }

    @Test
    public void emptyRefreshTokenLoginNegativeTest() {
        WithoutRefreshTokenLoginBodyModel emptyRefreshToken = new WithoutRefreshTokenLoginBodyModel();
        WithoutRefreshTokenLoginResponseModel emptyRefreshResponseModel = given(loginRequestSpec)
                .body(emptyRefreshToken)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(withoutRefreshTokenResponseSpec)
                .extract().as(WithoutRefreshTokenLoginResponseModel.class);

        String actualRefresh = emptyRefreshResponseModel.refresh().get(0);
        assertThat(actualRefresh).isEqualTo(td.expectedRequiredField);
    }

    @Test
    public void invalidRefreshTokenLoginNegativeTest() {
        InvalidRefreshTokenBodyModel invalidTokenBodyModel = new InvalidRefreshTokenBodyModel(td.expectedErrorInvalidRefreshToken);
        InvalidRefreshTokenResponseModel loginResponse = given(loginRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);

        String actualDetailInvalidRefreshToken = loginResponse.detail();
        String actualCodeInvalidRefreshToken = loginResponse.code();

        assertThat(actualDetailInvalidRefreshToken).isEqualTo(td.expectedErrorValidToken);
        assertThat(actualCodeInvalidRefreshToken).isEqualTo(td.expectedTokenNotValidCode);
    }

    @Test
    public void accessTokenInsteadRefreshTokenLoginNegativeTest() {
        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(td.username, td.password);

        given(registrationRequestSpec)

                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel data = new LoginBodyModel(td.username, td.password);

        SuccessfulLoginResponseModel refreshResponse = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
        String accessToken = refreshResponse.access();

        InvalidRefreshTokenBodyModel invalidTokenBodyModel = new InvalidRefreshTokenBodyModel(accessToken);
        InvalidRefreshTokenResponseModel loginResponse = given(loginRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);

        String actualDetailInvalidRefreshToken = loginResponse.detail();
        String actualCodeInvalidRefreshToken = loginResponse.code();

        assertThat(actualDetailInvalidRefreshToken).isEqualTo(td.expectedErrorWrongTokenType);
        assertThat(actualCodeInvalidRefreshToken).isEqualTo(td.expectedTokenNotValidCode);

    }

}
