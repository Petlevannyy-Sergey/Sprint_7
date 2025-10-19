package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.URIs;

import static io.restassured.RestAssured.given;

public class OrderActions {
    @Step("Создание заказа")
    public static Response create(Order order) {
        return given().header("Content-type", "application/json").and().body(order).when().post(URIs.ORDER);
    }

    @Step("Отмена заказа")
    public static void cancel(String track) {
        given().header("Content-type", "application/json").and().body(new Track(track)).when().put(URIs.CANCEL);
    }

    @Step("Получить список заказов")
    public static Response get() {
        return given().header("Content-type", "application/json").when().get(URIs.ORDER);
    }

    @Step("Получить заказ по его номеру")
    public static Response get(String track) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("t", track)
                .when()
                .get(URIs.GET_ORDER);
    }

    @Step("Принять заказ")
    public static Response accept(String orderId, String courierId) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .when()
                .put(URIs.ACCEPT_ORDER + "/" + orderId);
    }
}