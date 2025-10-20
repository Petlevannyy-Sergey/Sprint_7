package order;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {
    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getOrderListIsSuccess() {
        OrderActions.get().then().statusCode(HttpStatus.SC_OK).assertThat().body("orders", notNullValue());
    }
}
