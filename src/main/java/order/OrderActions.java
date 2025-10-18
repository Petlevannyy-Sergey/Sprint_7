package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.URIs;

import static io.restassured.RestAssured.given;

public class OrderActions {
    @Step("Создание заказа")
    public static Response create(Order order) {
        return given().header("Content-type", "application/json").and().body(order).when().post(URIs.ORDER);
    }

    @Step("Отмена заказа")
    public static void cancel(String track) {
       given().header("Content-type", "application/json").and().body(new Track(track));//.when().put(URIs.CANCEL_ORDER);
    }
}
