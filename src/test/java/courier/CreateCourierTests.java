package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.RandomUtils;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTests {
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courier = new Courier(RandomUtils.GenerateLogin(), RandomUtils.GeneratePassword(), RandomUtils.GenerateFirstName());
    }

    @After
    public void tearDown() {
        Response response = CourierActions.login(courier);
        if(response.then().extract().statusCode() == HttpStatus.SC_OK)
        {
            String id = response.then().extract().path("id").toString();
            CourierActions.delete(id);
        }
    }

    @Test
    @DisplayName("Создание курьера с использованием валидных данных")
    public void createNewCourierIsSuccess() {
        // Arrange

        // Act
        Response response = CourierActions.create(courier);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createTwoIdenticalCouriers() {
        // Arrange

        // Act
        CourierActions.create(courier);
        Response response = CourierActions.create(courier);

        // Assert
        response.then().assertThat().statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
