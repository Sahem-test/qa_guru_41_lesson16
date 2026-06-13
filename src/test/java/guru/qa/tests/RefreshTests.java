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

public class RefreshTests extends TestBase {

    TestData td = new TestData();

    @Test
    public void successfulRefreshTest() {

        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(td.username, td.password);

        given(registrationRequestSpec)

                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);
        RefreshBodyModel data = new RefreshBodyModel(td.username, td.password);

        SuccessfulRefreshResponseModel refreshResponse = given(refreshRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulRefreshResponseSpec)
                .extract().as(SuccessfulRefreshResponseModel.class);

        String expectedTokenPart = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = refreshResponse.access();
        String actualRefresh = refreshResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPart);
        assertThat(actualRefresh).startsWith(expectedTokenPart);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        RefreshBodyModel data = new RefreshBodyModel(td.username, td.wrongPassword);

        WrongCredentialsRefreshResponseModel loginResponse = given(refreshRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsRefreshResponseSpec)
                .extract().as(WrongCredentialsRefreshResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetailError = loginResponse.detail();

        assertThat(expectedDetailError).isEqualTo(actualDetailError);

    }

    @Test
    public void emptyFieldRefreshTest() {
        EmptyRefreshTokenBodyModel emptyRefreshToken = new EmptyRefreshTokenBodyModel();
        EmptyRefreshResponseModel emptyRefreshResponseModel = given(refreshRequestSpec)
                .body(emptyRefreshToken)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(emptyFieldRefreshResponseSpec)
                .extract().as(EmptyRefreshResponseModel.class);

        String actualRefresh = emptyRefreshResponseModel.refresh().get(0);
        assertThat(actualRefresh).isEqualTo(td.expectedRefreshWithEmptyField);
    }

    @Test
    public void invalidRefreshTokenTest() {
        InvalidRefreshTokenBodyModel invalidTokenBodyModel = new InvalidRefreshTokenBodyModel(td.invalidRefreshToken);
        InvalidRefreshTokenResponseModel loginResponse = given(refreshRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);

        String actualDetailInvalidRefreshToken = loginResponse.detail();
        String actualCodeInvalidRefreshToken = loginResponse.code();

        assertThat(actualDetailInvalidRefreshToken).isEqualTo(td.expectedDetailInvalidRefreshToken);
        assertThat(actualCodeInvalidRefreshToken).isEqualTo(td.expectedCodeInvalidRefreshToken);
    }

    @Test
    public void accessTokenInsteadRefreshTokenTest() {
        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(td.username, td.password);

        given(registrationRequestSpec)

                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        RefreshBodyModel data = new RefreshBodyModel(td.username, td.password);

        SuccessfulRefreshResponseModel refreshResponse = given(refreshRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulRefreshResponseSpec)
                .extract().as(SuccessfulRefreshResponseModel.class);
        String accessToken = refreshResponse.access();

        InvalidRefreshTokenBodyModel invalidTokenBodyModel = new InvalidRefreshTokenBodyModel(accessToken);
        InvalidRefreshTokenResponseModel loginResponse = given(refreshRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);

        String actualDetailInvalidRefreshToken = loginResponse.detail();
        String actualCodeInvalidRefreshToken = loginResponse.code();

        assertThat(actualDetailInvalidRefreshToken).isEqualTo(td.expectedDetailWrongTokenType);
        assertThat(actualCodeInvalidRefreshToken).isEqualTo(td.expectedCodeInvalidRefreshToken);

    }

}
