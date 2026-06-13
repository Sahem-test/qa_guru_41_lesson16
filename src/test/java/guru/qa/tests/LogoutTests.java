package guru.qa.tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutResponseModel;
import models.logout.WrongReusedRefreshTokenResponseModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.Test;
import testData.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;

public class LogoutTests extends TestBase {


    TestData td = new TestData();

    @Test
    public void successfulLogoutTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel data = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String refreshToken = responseLogin.refresh();

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Test
    public void logoutWithReusedRefreshTokenShouldReturn401Test() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel data = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String refreshToken = responseLogin.refresh();

        LogoutBodyModel logoutFirstData = new LogoutBodyModel(refreshToken);
        given(logoutRequestSpec)
                .body(logoutFirstData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);

        LogoutBodyModel logoutSecondData = new LogoutBodyModel(refreshToken);
        WrongReusedRefreshTokenResponseModel logoutResponse =
                given(logoutRequestSpec)
                        .body(logoutSecondData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(wrongRefreshTokenLogoutResponseSpec)
                        .extract().as(WrongReusedRefreshTokenResponseModel.class);

        String actualDetailReusedRefreshToken = logoutResponse.detail();
        String actualCodeReusedRefreshToken = logoutResponse.code();
        assertThat(actualDetailReusedRefreshToken).isEqualTo(td.expectedErrorTokenIsBlackListed);
        assertThat(actualCodeReusedRefreshToken).isEqualTo(td.expectedTokenNotValidCode);

    }
        @Test
    public void logoutWithoutRefreshTokenNegativeTest(){

            WithoutRefreshTokenLogoutBodyModel logoutData = new WithoutRefreshTokenLogoutBodyModel();
            WithoutRefreshTokenLogoutResponseModel logoutResponse =
            given(logoutRequestSpec)
                    .body(logoutData)
                    .when()
                    .post("/auth/logout/")
                    .then()
                    .spec(withoutRefreshTokenLogoutResponseSpec)
                    .extract().as(WithoutRefreshTokenLogoutResponseModel.class);


            String actualErrorWithoutRefreshToken = logoutResponse.refresh().get(0);
            assertThat(actualErrorWithoutRefreshToken).isEqualTo(td.expectedRequiredField);
    }
    @Test
    public void accessTokenInsteadOfRefreshTokenNegativeTest(){
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel data = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String accessToken = responseLogin.access();

        LogoutBodyModel logoutData = new LogoutBodyModel(accessToken);
        WrongReusedRefreshTokenResponseModel logoutResponse =
        given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongRefreshTokenLogoutResponseSpec)
                .extract().as(WrongReusedRefreshTokenResponseModel.class);

        String actualDetailReusedRefreshToken = logoutResponse.detail();
        String actualCodeReusedRefreshToken = logoutResponse.code();
        assertThat(actualDetailReusedRefreshToken).isEqualTo(td.expectedErrorWrongTokenType);
        assertThat(actualCodeReusedRefreshToken).isEqualTo(td.expectedTokenNotValidCode);

    }

}
