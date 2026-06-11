package guru.qa.tests.examples;

import models.registration.model_examples.lombok.RegistrationBodyLombokModel;
import models.registration.model_examples.lombok.RegistrationResponseLombokModel;
import models.registration.model_examples.pojo.RegistrationBodyPojoModel;
import models.registration.model_examples.pojo.RegistrationResponsePojoModel;
import models.registration.model_examples.records.ExistingUserResponseRecordsModel;
import models.registration.model_examples.records.RegistrationBodyRecordsModel;
import models.registration.model_examples.records.RegistrationResponseRecordsModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTest_with_model_examples {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.name().firstName();
    }

    @Test
    @Disabled
    public void successfulRegistrationTest_badPractice() {
        // url: // https://book-club.qa.guru/api/v1/users/register/
        // body: {
        //  "username": "Sahem",
        //  "password": "552255"
        //}
        // move to model
        String data = "{\"username\": \"" + username + "\", " +
                " \"password\": \"" + password + "\"}";

        given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("refresh", is(username))
                .body("id", notNullValue());
    }


    @Test
    @Disabled
    public void successfulRegistrationTest_with_pojo() {
        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);
        // Подход с помощью конструктора, применять до 4 значений
        //  RegistrationBodyPojoModel data = new RegistrationBodyPojoModel(userName,password);

        RegistrationResponsePojoModel registrationResponsePojoModel = given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponsePojoModel.class);

        assertEquals(username, registrationResponsePojoModel.getUsername());

    }

    @Test
    @Disabled
    public void successfulRegistrationTest_with_lombok() {
        // Чтобы включить конструктор без параметров, добавить в class анотацию @NoArgsConstructor
        RegistrationBodyLombokModel data = new RegistrationBodyLombokModel();
        data.setUsername(username);
        data.setPassword(password);
        // Подход с помощью конструктора, для включения добавить перед class анотацию @AllArgsConstructor
        //RegistrationBodyLombokModel data = new RegistrationBodyLombokModel(username,password);
        RegistrationResponseLombokModel registrationResponseLombokModel = given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseLombokModel.class);

        assertEquals(username, registrationResponseLombokModel.getUsername());
    }

    @Test
    @Disabled
    public void successfulRegistrationTest_with_records() {

        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(username, password);

        RegistrationResponseRecordsModel registrationResponseRecordModel = given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseRecordsModel.class);

        assertEquals(username, registrationResponseRecordModel.username());
    }


    @Test
    @Disabled
    public void unsupportedMediaType415Test() {

        String data = "{\"username\": \"" + username + "\", " +
                " \"password\": \"" + password + "\"}";

        given()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .statusCode(415);
    }

    @Test
    @Disabled
    public void negativeRegistration500Test() {
        String data = "{\"username\": \"" + username + "\", " +
                " \"password\": \"" + password + "\"}";

        given()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register")
                .then()
                .statusCode(415);
    }


    @Test
    @Disabled
    public void invalidUserName400Test() {
        String data = "{\"username\": \"" + username + "\", " +
                " \"password\": \"" + password + "\"}";

        given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(username))
                .body("id", notNullValue());
    }

    @Test
    @Disabled
    public void existingUserRegistration400Test() {
        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(username, password);

        given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(username))
                .body("id", notNullValue());
        ExistingUserResponseRecordsModel response =
        given()
                .log().all()
                .contentType(JSON)
                // .header("content-type", ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
               // .body("username[0]", is("A user with that username already exists."));
                .extract()
                .as(ExistingUserResponseRecordsModel.class);
        String expectedError = "A user with that username already exists.";
        assertEquals(expectedError,response.username().get(0));

    }


}
