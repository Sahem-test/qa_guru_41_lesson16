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

        SuccessfulRegistrationResponseModel registrationResponseRecordModel = given()
                .log().all()
                .contentType(JSON)
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath
                        ("schemas/registration/successful_registration_response_schemas.json"))
                .body("username", notNullValue())
                .body("remoteAddr", notNullValue())
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = registrationResponseRecordModel.username();

        assertThat(actualUsername).isEqualTo(username);
        assertThat(registrationResponseRecordModel.firstName()).isEmpty();
        assertThat(registrationResponseRecordModel.id()).isGreaterThan(0);
        assertThat(registrationResponseRecordModel.lastName()).isEmpty();
        assertThat(registrationResponseRecordModel.email()).isEmpty();

        String ipAddressRegexp = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

        assertThat(registrationResponseRecordModel.remoteAddr()).matches(ipAddressRegexp);
    }


    @Test
    public void wrongExistingUserRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                given()
                        .log().all()
                        .contentType(JSON)
                        .body(registrationData)
                        .basePath("/api/v1")
                        .when()
                        .post("/users/register/")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .body(matchesJsonSchemaInClasspath
                                ("schemas/registration/successful_registration_response_schemas.json"))
                        .body("username", notNullValue())
                        .body("id", notNullValue())
                        .extract()
                        .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = firstRegistrationResponse.username();
        assertThat(actualUsername).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse =
                given()
                        .log().all()
                        .contentType(JSON)
                        .body(registrationData)
                        .basePath("/api/v1")
                        .when()
                        .post("/users/register/")
                        .then()
                        .log().all()
                        .statusCode(400)
                        .body(matchesJsonSchemaInClasspath
                                ("schemas/registration/existing_user_registration_response_schemas.json"))
                        .body("username", notNullValue())
                        .extract()
                        .as(ExistingUserResponseModel.class);

        String expectedError = "A user with that username already exists.";
        String actualError = secondRegistrationResponse.username().get(0);

        assertThat(actualError).isEqualTo(expectedError);

    }




}
