package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBooking {
    private APIClient apiClient;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin","password123");
    }

    @Test
    public void testDeleteBooking() throws Exception {

        Response response = apiClient.deleteBooking(1);
        assertThat(response.getStatusCode()).isEqualTo(201);

    }
}
