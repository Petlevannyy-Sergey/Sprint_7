package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.runner.Request;
import utils.URIs;

import static io.restassured.RestAssured.given;

public class CourierActions {

    @Step("Создание курьера")
    public static Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(URIs.COURIER);
    }

    @Step("Вход в систему")
    public static Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(URIs.LOGIN);

    }

    @Step("Удаление курьера")
    public static Response delete(String id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(new DeleteCourier(id))
                .when()
                .delete(URIs.COURIER + "/" + id);
    }
}
