package login;

import courier.Courier;
import courier.CourierActions;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.RandomUtils;
import utils.URIs;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTests {
    private Courier courier;

    @Before
    public void setUp() {
        courier = new Courier(RandomUtils.generateLogin(), RandomUtils.generatePassword(), RandomUtils.generateFirstName());
        CourierActions.create(courier);
    }

    @After
    public void tearDown() {
        ValidatableResponse response = CourierActions.login(new Login(courier.getLogin(), courier.getPassword())).then();
        if (response.extract().statusCode() == HttpStatus.SC_OK) {
            String id = response.extract().path("id").toString();
            CourierActions.delete(id);
        }
    }

    @Test
    @DisplayName("Авторизация с валидными данными")
    public void loginCourierIsSuccess() {
        // Arrange
        Login login = new Login(courier.getLogin(), courier.getPassword());

        // Act
        Response response = CourierActions.login(login);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    public void loginCourierWithIncorrectLoginThrowsError() {
        // Arrange
        Login auth = new Login(RandomUtils.generateLogin(), courier.getPassword());

        // Act
        Response response = CourierActions.login(auth);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginCourierWithIncorrectPasswordThrowsError() {
        // Arrange
        Login auth = new Login(courier.getLogin(), RandomUtils.generatePassword());

        // Act
        Response response = CourierActions.login(auth);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")
    public void loginCourierWithIncorrectLoginAndPasswordThrowsError() {
        // Arrange
        Login auth = new Login(RandomUtils.generateLogin(), RandomUtils.generatePassword());

        // Act
        Response response = CourierActions.login(auth);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }
}
