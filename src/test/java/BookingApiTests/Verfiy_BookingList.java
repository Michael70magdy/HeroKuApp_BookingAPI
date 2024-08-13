package BookingApiTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class Verfiy_BookingList {

    private static final String BOOKING_LIST_URL = "https://restful-booker.herokuapp.com/booking";

    @Test
    public void VerifyBookingList() {
        Response response = RestAssured.given()
                .get(BOOKING_LIST_URL);

        Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200");

        List<Integer> bookingList = response.jsonPath().getList("bookingid", Integer.class);
        System.out.println("Number of bookings: " + bookingList.size());

        Assert.assertTrue(bookingList.size() > 0, "Booking list is empty");
    }
}
