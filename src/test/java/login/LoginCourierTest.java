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

import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courier = new Courier(RandomUtils.GenerateLogin(), RandomUtils.GeneratePassword(), RandomUtils.GenerateFirstName());
    }

    @After
    public void tearDown() {
        ValidatableResponse response = CourierActions.login(courier).then();
        if (response.extract().statusCode() == HttpStatus.SC_OK) {
            String id = response.extract().path("id").toString();
            CourierActions.delete(id);
        }
    }

    @Test
    @DisplayName("Вход в систему с валидными данными")
    public void loginCourierTestIsSuccess() {
        // Arrange

        // Act
        CourierActions.create(courier);
        Response response = CourierActions.login(courier);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", notNullValue());
    }
}
