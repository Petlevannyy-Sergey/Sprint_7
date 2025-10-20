package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import login.Login;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.RandomUtils;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)

public class CreateCourierParameterizedTests {
    private final String login;
    private final String password;
    private final String firstName;

    public CreateCourierParameterizedTests(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"", RandomUtils.generatePassword(), RandomUtils.generateFirstName()},
                {RandomUtils.generateLogin(), "", RandomUtils.generateFirstName()},
                {RandomUtils.generateLogin(), RandomUtils.generatePassword(), ""},
                {"", "", ""}
        };
    }

    @After
    public void tearDown() {
        Response response = CourierActions.login(new Login(login, password));
        if (response.then().extract().statusCode() == HttpStatus.SC_OK) {
            String id = response.then().extract().path("id").toString();
            CourierActions.delete(id);
        }
    }

    @Test
    @DisplayName("Создание курьера c одним не заполненным обязательным полем")
    public void createCourierWithInsufficientDataThrowsError() {
        // Arrange
        Courier courier = new Courier(login, password, firstName);

        // Act
        Response response = CourierActions.create(courier);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
