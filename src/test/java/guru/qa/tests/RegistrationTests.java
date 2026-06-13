package guru.qa.tests;

import models.registration.*;
import org.junit.jupiter.api.Test;
import testData.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.registration.RegistrationSpec.*;


public class RegistrationTests extends TestBase {
    TestData td = new TestData();

    @Test
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);

        SuccessfulRegistrationResponseModel registrationResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = registrationResponseModel.username();

        assertThat(actualUsername).isEqualTo(td.username);
        assertThat(registrationResponseModel.firstName()).isEmpty();
        assertThat(registrationResponseModel.id()).isGreaterThan(0);
        assertThat(registrationResponseModel.lastName()).isEmpty();
        assertThat(registrationResponseModel.email()).isEmpty();
        assertThat(registrationResponseModel.remoteAddr()).matches(td.ipAddressRegexp);
    }

    @Test
    public void existingUserRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                given(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationResponseSpec)
                        .extract()
                        .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = firstRegistrationResponse.username();
        assertThat(actualUsername).isEqualTo(td.username);

        ExistingUserResponseModel secondRegistrationResponse =
                given(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(wrongExistingUserRegistrationResponseSpec)
                        .extract()
                        .as(ExistingUserResponseModel.class);

        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(td.expectedErrorExistingUser);
    }

    @Test
    public void unsupportedMediaTypeRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);

        UnsupportedMediaTypeRegistrationBodyModel unsupportedMediaTypeResponseModel =
                given(unsupportedMediaTypeRegistrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(unsupportedMediaTypeRegistrationResponseSpec)
                        .extract()
                        .as(UnsupportedMediaTypeRegistrationBodyModel.class);

        String actualError = unsupportedMediaTypeResponseModel.detail();
        assertThat(actualError).isEqualTo(td.expectedErrorUnsupportedMediaType);
    }

    @Test
    public void emptyUsernameRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", td.password);

        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongUsernameResponseSpecification)
                .extract()
                .as(EmptyFieldUsernameResponseModel.class);

        String actualError = emptyFieldUsernameResponseModel.username().get(0);
        assertThat(actualError).isEqualTo(td.expectedErrorNotBeBlank);
    }

    @Test
    public void emptyPasswordRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, "");

        WrongPasswordResponseModel wrongPasswordResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
                .extract()
                .as(WrongPasswordResponseModel.class);

        String actualError = wrongPasswordResponseModel.password().get(0);
        assertThat(actualError).isEqualTo(td.expectedErrorNotBeBlank);
    }

    @Test
    public void passwordLongerRequiredLengthRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.longerRequiredLengthPassword);

        WrongPasswordResponseModel wrongPasswordResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
                .extract()
                .as(WrongPasswordResponseModel.class);

        String actualError = wrongPasswordResponseModel.password().get(0);
        assertThat(actualError).isEqualTo(td.expectedErrorLongerRequiredLengthPassword);

    }
}
