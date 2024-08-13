package BookingApiTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;

public class Create_Book {
    @Test
    public void create_Book() throws IOException {
        FileInputStream TestData = new FileInputStream("Booking_TestData.xlsx");

        Workbook workbook = new XSSFWorkbook(TestData);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);

        String firstname = row.getCell(0).getStringCellValue();
        String lastname = row.getCell(1).getStringCellValue();
        int totalprice = (int) row.getCell(2).getNumericCellValue();
        boolean depositpaid = row.getCell(3).getBooleanCellValue();
        String checkin = row.getCell(4).getStringCellValue();
        String checkout = row.getCell(5).getStringCellValue();
        String additionalneeds = row.getCell(6).getStringCellValue();


        workbook.close();
        TestData.close();


        String requestBody = "{\n" +
                "\"firstname\": \"" + firstname + "\",\n" +
                "\"lastname\": \"" + lastname + "\",\n" +
                "\"totalprice\": " + totalprice + ",\n" +
                "\"depositpaid\": " + depositpaid + ",\n" +
                "\"bookingdates\": {\n" +
                "\"checkin\": \"" + checkin + "\",\n" +
                "\"checkout\": \"" + checkout + "\"\n" +
                "},\n" +
                "\"additionalneeds\": \"" + additionalneeds + "\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("https://restful-booker.herokuapp.com/booking");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status 200");

        int bookingId = response.jsonPath().getInt("bookingid");
        Assert.assertTrue(bookingId > 0, "Booking ID is not greater than zero");

        Assert.assertEquals(response.jsonPath().getString("booking.firstname"), firstname);
        Assert.assertEquals(response.jsonPath().getString("booking.lastname"), lastname);
        Assert.assertEquals(response.jsonPath().getInt("booking.totalprice"), totalprice);
        Assert.assertTrue(response.jsonPath().getBoolean("booking.depositpaid"));
        Assert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), checkin);
        Assert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), checkout);
        Assert.assertEquals(response.jsonPath().getString("booking.additionalneeds"), additionalneeds);

        System.out.println("Booking ID: " + bookingId);

    }
}
