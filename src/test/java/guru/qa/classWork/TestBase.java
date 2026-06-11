package guru.qa.classWork;

import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
    }
}
