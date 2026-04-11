package core.models;

public class CreateNewBooking {
    private int bookingid;
    private BookingById booking;

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public BookingById getBooking() {
        return booking;
    }

    public void setBooking(BookingById booking) {
        this.booking = booking;
    }
}
