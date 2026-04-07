package tests;

import core.clients.APIClient;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingByIdTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }


    @Test
    public void testGetBookingById() throws Exception {

        Response response = apiClient.getBookingById(1);
        assertThat(response.getStatusCode()).isEqualTo(200);


        String responseBody = response.getBody().asString();
        BookingById bookings = objectMapper.readValue(responseBody, BookingById.class);

        assertThat(bookings.lastname).isEqualTo("Jones");

        System.out.println(bookings.lastname);
    }
}
