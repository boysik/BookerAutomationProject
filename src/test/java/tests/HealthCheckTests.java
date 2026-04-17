package tests;

import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


public class HealthCheckTests {
    private APIClient apiClient;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
    }

    @Test
    public void testPing() {
        step("Проверка соединения",()->{
            Response response = apiClient.ping();
            assertThat(response.getStatusCode()).isEqualTo(201);
        });
    }
}
