package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.Specification;
import utils.URIs;

import static io.restassured.RestAssured.given;

public class OrderActions {
    @Step("Создание заказа")
    public static Response create(Order order) {
        return given().spec(Specification.requestSpecification()).and().body(order).when().post(URIs.ORDER);
    }

    @Step("Отмена заказа")
    public static void cancel(String track) {
        given().spec(Specification.requestSpecification()).and().body(new Track(track)).when().put(URIs.CANCEL);
    }

    @Step("Получить список заказов")
    public static Response get() {
        return given().spec(Specification.requestSpecification()).when().get(URIs.ORDER);
    }

    @Step("Получить заказ по его номеру")
    public static Response get(String track) {
        return given()
                .spec(Specification.requestSpecification())
                .queryParam("t", track)
                .when()
                .get(URIs.GET_ORDER);
    }

    @Step("Принять заказ")
    public static Response accept(String orderId, String courierId) {
        return given()
                .spec(Specification.requestSpecification())
                .queryParam("courierId", courierId)
                .when()
                .put(URIs.ACCEPT_ORDER + "/" + orderId);
    }
}