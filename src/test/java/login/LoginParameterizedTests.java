package login;

import courier.CourierActions;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.RandomUtils;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class LoginParameterizedTests {
    private final String login;
    private final String password;

    public LoginParameterizedTests(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @After
    public void tearDown() {
        ValidatableResponse response = CourierActions.login(new Login(login, password)).then();
        if (response.extract().statusCode() == HttpStatus.SC_OK) {
            String id = response.extract().path("id").toString();
            CourierActions.delete(id);
        }
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"", RandomUtils.generatePassword()},
                {RandomUtils.generateLogin(), ""},
                {"", ""},
        };
    }

    @Test
    @DisplayName("Авторизация при частично незаполненных данных")
    public void LoginWithInsufficientCredentialsThrowsError() {
        // Arrange
        Login auth = new Login(login, password);

        // Act
        Response response = CourierActions.login(auth);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }
}
