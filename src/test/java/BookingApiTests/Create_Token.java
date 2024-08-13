package BookingApiTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Create_Token {

    private static final String AUTH_URL = "https://restful-booker.herokuapp.com/auth";

    @Test
    public void CreateToken() {
        String requestBody = "{ \"username\": \"admin\", \"password\": \"password123\" }";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(AUTH_URL);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status 200");

        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.isEmpty(), "Response body is empty");

        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token is null");
        Assert.assertFalse(token.isEmpty(), "Token is empty");

        System.out.println("Generated Token: " + token);
    }

}
