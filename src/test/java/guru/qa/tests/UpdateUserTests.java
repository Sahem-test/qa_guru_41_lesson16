package guru.qa.tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.user.*;
import org.junit.jupiter.api.Test;
import testData.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static specs.user.UpdateUserSpec.*;

public class UpdateUserTests extends TestBase {
    TestData td = new TestData();

    @Test
    public void successfulFullUpdateUserWithPutTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel dataLogin = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(dataLogin)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String accessToken = responseLogin.access();

        SuccessfulUpdateUserBodyModel dataUpdateUser = new SuccessfulUpdateUserBodyModel(td.username, td.firstName,
                td.lastName, td.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser =
                given(updateUserRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(dataUpdateUser)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract().as(SuccessfulUpdateUserResponseModel.class);

        String actualUsername = responseUpdateUser.username();
        String actualFirstName = responseUpdateUser.firstName();
        String actualLastName = responseUpdateUser.lastName();
        String actualEmail = responseUpdateUser.email();

        assertThat(responseUpdateUser.id()).isPositive();
        assertThat(actualUsername).isEqualTo(td.username);
        assertThat(actualFirstName).isEqualTo(td.firstName);
        assertThat(actualLastName).isEqualTo(td.lastName);
        assertThat(actualEmail).isEqualTo(td.email);
        assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
    }

    @Test
    public void successfulFullUpdateUserWithPatchTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel dataLogin = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(dataLogin)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String accessToken = responseLogin.access();

        SuccessfulUpdateUserBodyModel dataUpdateUser = new SuccessfulUpdateUserBodyModel(td.username, td.firstName,
                td.lastName, td.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser =
                given(updateUserRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(dataUpdateUser)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract().as(SuccessfulUpdateUserResponseModel.class);

        String actualUsername = responseUpdateUser.username();
        String actualFirstName = responseUpdateUser.firstName();
        String actualLastName = responseUpdateUser.lastName();
        String actualEmail = responseUpdateUser.email();

        assertThat(responseUpdateUser.id()).isPositive();
        assertThat(actualUsername).isEqualTo(td.username);
        assertThat(actualFirstName).isEqualTo(td.firstName);
        assertThat(actualLastName).isEqualTo(td.lastName);
        assertThat(actualEmail).isEqualTo(td.email);
        assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
    }

    @Test
    public void successfulPartialUpdateUserWithPatchTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel dataLogin = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(dataLogin)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String accessToken = responseLogin.access();

        PartialUpdateUserBodyModel dataUpdateUser =
                new PartialUpdateUserBodyModel(td.username, td.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser =
                given(updateUserRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(dataUpdateUser)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract().as(SuccessfulUpdateUserResponseModel.class);

        String actualUsername = responseUpdateUser.username();
        String actualFirstName = responseUpdateUser.firstName();
        String actualLastName = responseUpdateUser.lastName();
        String actualEmail = responseUpdateUser.email();

        assertThat(responseUpdateUser.id()).isPositive();
        assertThat(actualUsername).isEqualTo(td.username);
        assertThat(actualEmail).isEqualTo(td.email);
        assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
    }

    @Test
    public void partialUpdateUserWithPutNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel dataLogin = new LoginBodyModel(td.username, td.password);
        SuccessfulLoginResponseModel responseLogin = given(loginRequestSpec)
                .body(dataLogin)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String accessToken = responseLogin.access();

        PartialUpdateUserBodyModel dataUpdateUser =
                new PartialUpdateUserBodyModel(td.username, td.email);
        UnsuccessfulPartialUpdateUserResponseModel responseUpdateUser =
                given(updateUserRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(dataUpdateUser)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(wrongPartialUpdateUseResponseSpec)
                        .extract().as(UnsuccessfulPartialUpdateUserResponseModel.class);

        String actualFirstName = responseUpdateUser.firstName().get(0);
        String actualLastName = responseUpdateUser.lastName().get(0);

        assertThat(actualFirstName).isEqualTo(td.expectedRequiredField);
        assertThat(actualLastName).isEqualTo(td.expectedRequiredField);
        assertThat(responseUpdateUser.username()).isNull();
        assertThat(responseUpdateUser.email()).isNull();
    }

    @Test
    public void withoutRequiredAuthorizationHeaderUpdateUserNegativeTest() {

        SuccessfulUpdateUserBodyModel dataUpdateUser = new SuccessfulUpdateUserBodyModel(td.username, td.firstName,
                td.lastName, td.email);
        UnauthorizedResponseModel responseUpdateUser =
                given(updateUserRequestSpec)
                        .body(dataUpdateUser)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(unauthorizedResponseSpec)
                        .extract().as(UnauthorizedResponseModel.class);

        String actualDetail = responseUpdateUser.detail();
        assertThat(actualDetail).isEqualTo(td.expectedErrorAuthorized);
    }
}




