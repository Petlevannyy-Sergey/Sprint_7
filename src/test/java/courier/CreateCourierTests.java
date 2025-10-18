package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.RandomUtils;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTests {
    private String login;
    private String password;
    private String firstName;
    private String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        login = RandomUtils.GenerateLogin();
        password = RandomUtils.GeneratePassword();
        firstName = RandomUtils.GenerateFirstName();
    }

    @After
    public void tearDown() {
        CourierActions.delete(id);
    }

    @Test
    @DisplayName("Создание курьера с использованием валидных данных")
    public void createNewCourierIsSuccess() {
        // Arrange
        Courier courier = new Courier(login, password, firstName);

        // Act
        Response response = CourierActions.create(courier);
        // id для удаления курьера после прохождения теста
        id = CourierActions.login(courier).then().extract().path("id").toString();

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createTwoIdenticalCouriers() {
        // Arrange
        Courier courier = new Courier(login, password, firstName);

        // Act
        CourierActions.create(courier);
        Response response = CourierActions.create(courier);
        // id для удаления курьера после прохождения теста
        id = CourierActions.login(courier).then().extract().path("id").toString();

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
