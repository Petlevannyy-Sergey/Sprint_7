package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import utils.URIs;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = URIs.BASE_URI;
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getOrderListIsSuccess() {
        OrderActions.get().then().statusCode(HttpStatus.SC_OK).assertThat().body("orders", notNullValue());
    }
}
