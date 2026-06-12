package testData;

import net.datafaker.Faker;

import java.util.Locale;

public class TestData {

    public static Faker faker = new Faker();

    public String
            username = faker.name().firstName(),
            password = faker.name().firstName(),
            longerRequiredLengthPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            ipAddressRegexp = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$",
            existingUserExpectedError = "A user with that username already exists.",
            unsupportedMediaTypeExpectedError = "Unsupported media type \"text/plain; charset=ISO-8859-1\" in request.",
            emptyFieldUsernameExpectedError = "This field may not be blank.",
            emptyFieldPasswordExpectedError = "This field may not be blank.",
            longerRequiredLengthPasswordExpectedError = "Ensure this field has no more than 128 characters.";
}
