package guru.qa.classWork;

import models.pojo.RegistrationBodyPojoModel;
import models.pojo.RegistrationResponsePojoModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTest {

    @Test
    public void successfulRegistrationTest_badPractice() {
        Faker faker = new Faker();
        String userName = faker.name().firstName();
        String password = faker.name().firstName();
        // url: // https://book-club.qa.guru/api/v1/users/register/
        // body: {
        //  "username": "Sahem",
        //  "password": "552255"
        //}
        // move to model
        String data = "{\"username\": \"" + userName + "\", " +
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
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void successfulRegistrationTest_badPractice_with_pojo() {
        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.name().firstName();

        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);
        // Подход с помощью конструктора, применять до 4 значений
      //  RegistrationBodyPojoModel data = new RegistrationBodyPojoModel(userName,password);

        RegistrationResponsePojoModel  registrationResponsePojoModel = given()
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
    public void unsupportedMediaType415Test() {

        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String password = faker.name().firstName();

        String data = "{\"username\": \"" + userName + "\", " +
                " \"password\": \"" + password + "\"}";

        given()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .statusCode(415);
    }

    @Test
    public void negativeRegistration500Test() {
        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String password = faker.name().firstName();

        String data = "{\"username\": \"" + userName + "\", " +
                " \"password\": \"" + password + "\"}";

        given()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register")
                .then()
                .statusCode(415);
    }


    @Test
    public void invalidUserName400Test() {
        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String password = faker.name().firstName();
        String data = "{\"username\": \"" + userName + "\", " +
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
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void existingUserRegistration400Test() {
        Faker faker = new Faker();
        String userName = faker.name().firstName();
        String password = faker.name().firstName();

        String data = "{\"username\": \"" + userName + "\", " +
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
                .body("username", is(userName))
                .body("id", notNullValue());

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
                .body("username[0]", is("A user with that username already exists."));

    }


}
