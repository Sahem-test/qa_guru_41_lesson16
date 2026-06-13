package testData;

import net.datafaker.Faker;

public class TestData {

    public static Faker faker = new Faker();

    public String
            username = faker.name().firstName(),
            firstName = faker.name().firstName(),
            lastName = faker.name().lastName(),
            email = faker.internet().emailAddress(),
            password = faker.regexify("[A-Za-z0-9]{8}"),
            wrongPassword = faker.name().fullName(),
            longerRequiredLengthPassword = "a".repeat(129),

    ipAddressRegexp = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$",
            expectedErrorInvalidUsernameOrPassword = "Invalid username or password.",
            expectedErrorAuthorized = "Authentication credentials were not provided.",
            expectedErrorExistingUser = "A user with that username already exists.",
            expectedErrorUnsupportedMediaType = "Unsupported media type \"text/plain; charset=ISO-8859-1\" in request.",
            expectedErrorNotBeBlank = "This field may not be blank.",
            expectedErrorLongerRequiredLengthPassword = "Ensure this field has no more than 128 characters.",
            expectedRequiredField = "This field is required.",
            expectedErrorInvalidRefreshToken = "something text",
            expectedErrorValidToken = "Token is invalid",
            expectedErrorWrongTokenType = "Token has wrong type",
            expectedErrorTokenIsBlackListed = "Token is blacklisted",
            expectedErrorTokenNotValid = "token_not_valid";

}
