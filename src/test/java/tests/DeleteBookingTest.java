package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin","password123");
    }

    public int getRandomNumber(int max) {
        // Генерирует случайное число от 1 до max (включительно)
        return (int) (Math.random() * max) + 1;
    }

    @Test
    public void testDeleteBooking() throws Exception {
        //Делаем запрос и записываем в переменную ответ
        Response getBookingId = apiClient.getBooking();

        //Десериализуем JSON
        String responseBody = getBookingId.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody, new TypeReference<>() {});

        //Проверяем что ответ не пустой
        assertThat(bookings).isNotEmpty();

        //Получаем рандомный id по размеру ответ
        int bookingExactId = getRandomNumber(bookings.size() -1);

        Response response = apiClient.deleteBooking(bookingExactId);
        assertThat(response.getStatusCode()).isEqualTo(201);

        Response getDeletedBooking = apiClient.getBookingAfterDeleteById(bookingExactId);
        assertThat(getDeletedBooking.getStatusCode()).isEqualTo(404);
    }
}
