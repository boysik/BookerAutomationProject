package tests;

import core.clients.APIClient;
import core.models.BookingById;
import core.models.BookingDates;
import core.models.CreateNewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingByIdTest {
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
    public void testGetBookingById() throws Exception {
        String requestBody = objectMapper.writeValueAsString(booking);
        Response postResponse = apiClient.createBooking(requestBody);

        assertThat(postResponse.getStatusCode()).isEqualTo(200);

        String postResponseBody = postResponse.asString();
        assertThat(postResponseBody).isNotNull();

        createNewBooking = objectMapper.readValue(postResponseBody, CreateNewBooking.class);

        Response response = apiClient.getBookingById(createNewBooking.getBookingid());
        assertThat(response.getStatusCode()).isEqualTo(200);

        assertEquals(createNewBooking.getBooking().getFirstname(), booking.getFirstname());
        assertEquals(createNewBooking.getBooking().getLastname(), booking.getLastname());
        assertEquals(createNewBooking.getBooking().getTotalprice(), booking.getTotalprice());
        assertEquals(createNewBooking.getBooking().isDepositpaid(), booking.isDepositpaid());
        assertEquals(createNewBooking.getBooking().getBookingdates().getCheckin(), booking.getBookingdates().getCheckin());
        assertEquals(createNewBooking.getBooking().getBookingdates().getCheckout(), booking.getBookingdates().getCheckout());
        assertEquals(createNewBooking.getBooking().getAdditionalneeds(), booking.getAdditionalneeds());
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin","password123");
        apiClient.deleteBooking(createNewBooking.getBookingid());

        assertThat(apiClient.getBookingAfterDeleteById(createNewBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}
