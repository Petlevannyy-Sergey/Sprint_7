package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.URIs;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderByNumberTests {
    String track;

    @Before
    public void setUp() {
        RestAssured.baseURI = URIs.BASE_URI;
    }

    @After
    public void tearDown() {
        if (track != null) {
            OrderActions.cancel(track);

        }
    }

    @Test
    @DisplayName("Получить заказ по валидному номеру")
    public void GetOrderByValidNumberIsSuccess() {
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
                List.of(ScooterColours.BLACK, ScooterColours.GREY));

        // Act
        Response orderResponse = OrderActions.create(order);
        track = orderResponse.then().extract().path("track").toString();
        Response response = OrderActions.get(track);

        // Assert
        response.then().statusCode(HttpStatus.SC_OK).assertThat().body("order", notNullValue());
    }

    @Test
    @DisplayName("Номер заказа не указан")
    public void GetOrderWithoutNumberThrowsError() {
        // Arrange

        // Act
        Response response = OrderActions.get(track);

        // Assert
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST).assertThat().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Неверно указан номер заказа")
    public void GetOrderWithIncorrectNumberThrowsError() {
        // Arrange

        // Act
        Response response = OrderActions.get("111");

        // Assert
        response.then().statusCode(HttpStatus.SC_NOT_FOUND).assertThat().body("message", equalTo("Заказ не найден"));
    }
}
