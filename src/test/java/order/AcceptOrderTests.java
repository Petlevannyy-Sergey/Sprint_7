package order;

import courier.Courier;
import courier.CourierActions;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import login.Login;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import utils.RandomUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class AcceptOrderTests {
    String track;
    String id;

    @After
    public void tearDown() {
        if (id != null) {
            CourierActions.delete(id);
        }

        if (track != null) {
            OrderActions.cancel(track);
        }
    }

    @Test
    @DisplayName("Принятие заказа с валидными данными")
    public void acceptOrderIsSuccess() {
        // Arrange
        Order order = new Order("Василий", "Васильев", "Москва Кремль дом 1", "Лубянка", "+79876543221", 5, "2025-11-02", "Позвонить за час", List.of(ScooterColours.BLACK, ScooterColours.GREY));
        Courier courier = new Courier(RandomUtils.generateLogin(), RandomUtils.generatePassword(), RandomUtils.generateFirstName());

        // Act
        Response orderResponse = OrderActions.create(order);
        track = orderResponse.then().extract().path("track").toString();

        CourierActions.create(courier);
        id = CourierActions.login(new Login(courier.getLogin(), courier.getPassword())).then().extract().path("id").toString();

        Response acceptResponse = OrderActions.accept(track, id);

        // Assert
        acceptResponse.then().statusCode(HttpStatus.SC_OK).assertThat().body("ok", equalTo("true"));
    }

    @Test
    @DisplayName("Принятие заказа без id курьера")
    public void acceptOrderWithoutCourierIdThrowsError() {
        // Arrange
        Order order = new Order("Василий", "Васильев", "Москва Кремль дом 1", "Лубянка", "+79876543221", 5, "2025-11-02", "Позвонить за час", List.of(ScooterColours.BLACK, ScooterColours.GREY));

        // Act
        Response orderResponse = OrderActions.create(order);
        track = orderResponse.then().extract().path("track").toString();
        Response acceptResponse = OrderActions.accept(track, "");

        // Assert
        acceptResponse.then().statusCode(HttpStatus.SC_BAD_REQUEST).assertThat().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с некорректным id курьера")
    public void acceptOrderWithIncorrectCourierIdThrowsError() {
        // Arrange
        Order order = new Order("Василий", "Васильев", "Москва Кремль дом 1", "Лубянка", "+79876543221", 5, "2025-11-02", "Позвонить за час", List.of(ScooterColours.BLACK, ScooterColours.GREY));

        // Act
        Response orderResponse = OrderActions.create(order);
        track = orderResponse.then().extract().path("track").toString();
        Response acceptResponse = OrderActions.accept(track, "111");

        // Assert
        acceptResponse.then().statusCode(HttpStatus.SC_NOT_FOUND).assertThat().body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Принятие заказа без номера")
    public void acceptOrderWithoutIdThrowsError() {
        // Arrange
        Courier courier = new Courier(RandomUtils.generateLogin(), RandomUtils.generatePassword(), RandomUtils.generateFirstName());

        // Act
        CourierActions.create(courier);
        id = CourierActions.login(new Login(courier.getLogin(), courier.getPassword())).then().extract().path("id").toString();
        Response acceptResponse = OrderActions.accept("", id);

        // Assert
        acceptResponse.then().statusCode(HttpStatus.SC_BAD_REQUEST).assertThat().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с неверным номером")
    public void acceptOrderWithWrongIdThrowsError() {
        // Arrange
        Courier courier = new Courier(RandomUtils.generateLogin(), RandomUtils.generatePassword(), RandomUtils.generateFirstName());

        // Act
        CourierActions.create(courier);
        id = CourierActions.login(new Login(courier.getLogin(), courier.getPassword())).then().extract().path("id").toString();
        Response acceptResponse = OrderActions.accept("111", id);

        // Assert
        acceptResponse.then().statusCode(HttpStatus.SC_NOT_FOUND).assertThat().body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Нельзя принять один тот же заказ дважды")
    public void acceptOrderTwiceThrowsError() {
        // Arrange
        Order order = new Order("Василий", "Васильев", "Москва Кремль дом 1", "Лубянка", "+79876543221", 5, "2025-11-02", "Позвонить за час", List.of(ScooterColours.BLACK, ScooterColours.GREY));
        Courier courier = new Courier(RandomUtils.generateLogin(), RandomUtils.generatePassword(), RandomUtils.generateFirstName());

        // Act
        Response orderResponse = OrderActions.create(order);
        track = orderResponse.then().extract().path("track").toString();

        CourierActions.create(courier);
        id = CourierActions.login(new Login(courier.getLogin(), courier.getPassword())).then().extract().path("id").toString();

        OrderActions.accept(track, id);
        Response acceptResponse = OrderActions.accept(track, id);

        // Assert
        acceptResponse.then().statusCode(HttpStatus.SC_CONFLICT).assertThat().body("message", equalTo("Этот заказ уже в работе"));
    }
}
