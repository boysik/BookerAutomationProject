package tests;

import core.clients.APIClient;
import core.models.Booking;
import com.fasterxml.jackson.core.type.TypeReference;
import core.models.BookingById;
import core.models.BookingDates;
import core.models.CreateNewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateNewBooking createNewBooking;
    private BookingById booking;


    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        booking = new BookingById();
        booking.setFirstname("Sally");
        booking.setLastname("Brown");
        booking.setTotalprice(111);
        booking.setDepositpaid(true);
        booking.setBookingdates(new BookingDates("2026-04-11", "2026-04-15"));
        booking.setAdditionalneeds("Towels");
    }

    @Test
    public void testGetBooking() throws Exception {

        String requestBody = objectMapper.writeValueAsString(booking);
        Response postResponse = apiClient.createBooking(requestBody);

        assertThat(postResponse.getStatusCode()).isEqualTo(200);

        String postResponseBody = postResponse.asString();
        assertThat(postResponseBody).isNotNull();
        createNewBooking = objectMapper.readValue(postResponseBody, CreateNewBooking.class);

        Response response = apiClient.getBooking();
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody, new TypeReference<>() {});

        assertThat(bookings).isNotEmpty();

        for (Booking booking : bookings) {
            assertThat(booking.getBookingId()).isGreaterThan(0);
        }
    }
    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin","password123");
        apiClient.deleteBooking(createNewBooking.getBookingid());

        assertThat(apiClient.getBookingAfterDeleteById(createNewBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}