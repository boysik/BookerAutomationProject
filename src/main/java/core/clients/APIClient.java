package core.clients;

import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClient {
    private final String baseUrl;
    private String token;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found" + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Configuration file not found" + configFileName, e);
        }
        return properties.getProperty("baseUrl");
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter());
    }

    public void createToken(String username, String password) {
        String requestBody = String.format("{ \"username\": \"%s\",\"password\": \"%s\" }", username, password);
        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath()) // POST-запрос на эндпоинт аутентификации
                .then()
                .statusCode(200) // Проверяем, что статус ответа 200 (ОК)
                .extract()
                .response();
        token = response.jsonPath().getString("token");
    }

    private Filter addAuthTokenFilter() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) -> {
            if (token != null) {
                requestSpec.header("Cookie", "token=" + token);
            }
            return ctx.next(requestSpec, responseSpec);
        };
    }

    public Response ping() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.PING.getPath())
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response getBookingAfterDeleteById(int bookingId) {
        return getRequestSpec()
                .pathParam("id",bookingId)
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(404)
                .extract()
                .response();
    }

    public Response getBookingById(int bookingId) {
        return getRequestSpec()
                .pathParam("id",bookingId)
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(404)
                .extract()
                .response();
    }

    public Response deleteBooking(int bookingId) {
        return getRequestSpec()
                .pathParam("id",bookingId)
                .when()
                .delete(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .response();
    }
    public Response createBooking(String createBooking) {
        return getRequestSpec()
                .body(createBooking)
                .when()
                .post(ApiEndpoints.BOOKING.getPath())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
    }
}