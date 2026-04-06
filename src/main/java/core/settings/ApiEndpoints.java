package core.settings;

public enum ApiEndpoints {
    PING("/ping"),
    BOOKING("/booking"),
    BOOKINGBYID("/booking/1");

    private final String path;

    ApiEndpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
