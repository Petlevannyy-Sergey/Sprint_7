package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderParameterizedTests {
    private final List<String> colors;
    private String track;

    public OrderParameterizedTests(List<String> colors) {
        this.colors = colors;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown() {
        OrderActions.cancel(track);
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {List.of(ScooterColours.BLACK)},
                {List.of(ScooterColours.GREY)},
                {List.of(ScooterColours.BLACK, ScooterColours.GREY)},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами самоката")
    public void createOrderIsSuccess() {
        // Arrange
        Order order = new Order(
                "Василий",
                "Васильев",
                "Москва Кремль дом 1",
                "Лубянка",
                "+79876543221",
                5,
                "2025-11-02",
                "Позвонить за час",
                colors);

        // Act
        Response response = OrderActions.create(order);
        track = response.then().extract().path("track").toString();

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("track", notNullValue());
    }
}