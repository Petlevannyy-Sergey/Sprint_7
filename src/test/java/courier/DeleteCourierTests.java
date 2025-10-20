package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import login.Login;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.RandomUtils;

import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTests {
    String id;

    @Before
    public void setUp() {
        Courier courier = new Courier(RandomUtils.generateLogin(),
                RandomUtils.generatePassword(),
                RandomUtils.generateFirstName());

        CourierActions.create(courier);
        id = CourierActions.login(new Login(courier.getLogin(), courier.getPassword())).then().extract().path("id").toString();
    }

    @After
    public void tearDown() {
        CourierActions.delete(id);
    }

    @Test
    @DisplayName("Удаление курьера с валидным id")
    public void deleteCourierWithCorrectIdIsSuccess() {
        // Arrange

        // Act
        Response response = CourierActions.delete(id);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера (запрос без id)")
    public void deleteCourierWithUnsetIdThrowsError() {
        // Arrange

        // Act
        Response response = CourierActions.delete("");

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера с неверным id")
    public void deleteCourierWithIncorrectIdThrowsError() {
        // Arrange

        // Act
        Response response = CourierActions.delete("222");

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id нет."));
    }
}
