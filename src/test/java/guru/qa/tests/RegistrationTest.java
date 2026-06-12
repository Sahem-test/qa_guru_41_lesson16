package guru.qa.tests;

import models.registration.*;
import org.junit.jupiter.api.Test;
import testData.TestData;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.registration.RegistrationSpec.*;


public class RegistrationTest extends TestBase {
    TestData td = new TestData();

    @Test
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username,td.password);

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
    public void wrongExistingUserRegistrationTest() {
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
        assertThat(actualError).isEqualTo(td.existingUserExpectedError);
    }

    @Test
    public void unsupportedMediaTypeTest() {
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
        assertThat(actualError).isEqualTo(td.unsupportedMediaTypeExpectedError);
    }

    @Test
    public void emptyFieldUsernameWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", td.password);

        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel = given(registrationRequestSpec)
//                .log().all()
//                .contentType(JSON)
//                .basePath("/api/v1")
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongUsernameResponseSpecification)
                //.log().all()
               // .statusCode(400)
//                .body(matchesJsonSchemaInClasspath
//                        ("schemas/registration/wrong_username_registration_response_schemas.json"))
                .extract()
                .as(EmptyFieldUsernameResponseModel.class);

        String actualError = emptyFieldUsernameResponseModel.username().get(0);
        assertThat(actualError).isEqualTo(td.emptyFieldUsernameExpectedError);
    }

    @Test
    public void emptyFieldPasswordWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, "");

        WrongPasswordResponseModel wrongPasswordResponseModel = given(registrationRequestSpec)
//                .log().all()
//                .contentType(JSON)
//                .basePath("/api/v1")
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
//                .log().all()
//                .statusCode(400)
//                .body(matchesJsonSchemaInClasspath
//                        ("schemas/registration/wrong_password_registration_response_schemas.json"))
                .extract()
                .as(WrongPasswordResponseModel.class);

        String actualError = wrongPasswordResponseModel.password().get(0);
        assertThat(actualError).isEqualTo(td.emptyFieldPasswordExpectedError);
    }

    @Test
    public void longerRequiredLengthWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.longerRequiredLengthPassword);

        WrongPasswordResponseModel wrongPasswordResponseModel = given(registrationRequestSpec)
//                .log().all()
//                .contentType(JSON)
//                .basePath("/api/v1")
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
//                .log().all()
//                .statusCode(400)
//                .body(matchesJsonSchemaInClasspath
//                        ("schemas/registration/wrong_password_registration_response_schemas.json"))
                .extract()
                .as(WrongPasswordResponseModel.class);

        String actualError = wrongPasswordResponseModel.password().get(0);
        assertThat(actualError).isEqualTo(td.longerRequiredLengthPasswordExpectedError);

    }
}
