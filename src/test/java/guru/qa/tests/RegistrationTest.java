package guru.qa.tests;

import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static specs.registration.RegistrationSpec.*;


public class RegistrationTest extends TestBase{

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.name().firstName();
    }



    @Test
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = registrationResponseModel.username();

        assertThat(actualUsername).isEqualTo(username);
        assertThat(registrationResponseModel.firstName()).isEmpty();
        assertThat(registrationResponseModel.id()).isGreaterThan(0);
        assertThat(registrationResponseModel.lastName()).isEmpty();
        assertThat(registrationResponseModel.email()).isEmpty();

        String ipAddressRegexp = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

        assertThat(registrationResponseModel.remoteAddr()).matches(ipAddressRegexp);
    }


    @Test
    public void wrongExistingUserRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
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
        assertThat(actualUsername).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse =
                given(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(wrongExistingUserRegistrationResponseSpec)
                        .extract()
                        .as(ExistingUserResponseModel.class);

        String expectedError = "A user with that username already exists.";
        String actualError = secondRegistrationResponse.username().get(0);

        assertThat(actualError).isEqualTo(expectedError);

    }




}
