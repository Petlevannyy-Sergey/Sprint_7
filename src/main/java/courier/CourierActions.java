package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import login.Login;
import utils.Specification;
import utils.URIs;

import static io.restassured.RestAssured.given;

public class CourierActions {

    @Step("Создание курьера")
    public static Response create(Courier courier) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(URIs.COURIER);
    }

    @Step("Удаление курьера")
    public static Response delete(String id) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(new DeleteCourier(id))
                .when()
                .delete(URIs.COURIER + "/" + id);
    }

    @Step("Авторизация")
    public static Response login(Login login) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(login)
                .when()
                .post(URIs.LOGIN);

    }
}
