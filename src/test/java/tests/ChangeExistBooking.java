package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingById;
import core.models.BookingDates;
import core.models.CreateNewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeExistBooking {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateNewBooking createNewBooking;
    private BookingById booking;
    private BookingById updatedBooking;


    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");

        booking = new BookingById();
        booking.setFirstname("Sally");
        booking.setLastname("Brown");
        booking.setTotalprice(111);
        booking.setDepositpaid(true);
        booking.setBookingdates(new BookingDates("2026-04-11", "2026-04-15"));
        booking.setAdditionalneeds("Towels");
    }

    @Test
    public void testChangeBooking() throws Exception {


        step("Проверка создания Booking", () -> {
            String requestBody = objectMapper.writeValueAsString(booking);
            Response response = apiClient.createBooking(requestBody);
            assertThat(response.getStatusCode()).isEqualTo(200);

            String responseBody = response.asString();
            assertThat(responseBody).isNotNull();

            createNewBooking = objectMapper.readValue(responseBody, CreateNewBooking.class);
        });

        step("Проверка что изменение Booking вернуло статус код 200", () -> {

                    booking.setFirstname("Sally");
                    booking.setLastname("Brown");
                    booking.setTotalprice(555);
                    booking.setDepositpaid(true);
                    booking.setBookingdates(new BookingDates("2026-04-11", "2026-04-15"));
                    booking.setAdditionalneeds("Towels");

                    String putRequestBody = objectMapper.writeValueAsString(booking);
                    Response putResponse = apiClient.changeBooking(putRequestBody, createNewBooking.getBookingid());

                    assertThat(putResponse.getStatusCode()).isEqualTo(200);

                    String putResponseBody = putResponse.asString();
                    assertThat(putResponseBody).isNotNull();
                    updatedBooking = objectMapper.readValue(putResponseBody, BookingById.class);

                    assertEquals(updatedBooking.getTotalprice(), booking.getTotalprice());
                }
        );

    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(createNewBooking.getBookingid());

        assertThat(apiClient.getBookingAfterDeleteById(createNewBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }

}
