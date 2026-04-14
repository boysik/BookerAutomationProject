package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingById;
import core.models.BookingDates;
import core.models.CreateNewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingWithFilters {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateNewBooking createNewBooking;
    private BookingById booking;
    private BookingById updatedBooking;

    List<Integer> bookingIds = new ArrayList<>();


    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        booking = new BookingById();

        bookingIds.add(createBooking("Sally","Wong","2026-04-15","2026-04-21"));
        bookingIds.add(createBooking("Peter","Green","2026-03-01","2026-03-10"));
        bookingIds.add(createBooking("Alice","Wonder","2026-06-01","2026-06-17"));
    }

    public int createBooking(String firstName, String lastName, String checkIn, String checkOut) throws JsonProcessingException {
        booking.setFirstname(firstName);
        booking.setLastname(lastName);
        booking.setTotalprice(111);
        booking.setDepositpaid(true);
        booking.setBookingdates(new BookingDates(checkIn, checkOut));
        booking.setAdditionalneeds("Towels");

        String requestBody = objectMapper.writeValueAsString(booking);
        Response postResponse = apiClient.createBooking(requestBody);

        assertThat(postResponse.getStatusCode()).isEqualTo(200);

        String postResponseBody = postResponse.asString();
        assertThat(postResponseBody).isNotNull();
        createNewBooking = objectMapper.readValue(postResponseBody, CreateNewBooking.class);

        return createNewBooking.getBookingid();
    }

    @Test
    public void testGetBooking() throws Exception {

        Response response = apiClient.getBookingWithFilters();
        assertThat(response.getStatusCode()).isEqualTo(200);


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
