package org.example.Companies;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetCompsTest {

    @Test
    public void testGetComps() {
        GetComps getComps = new GetComps();

        Response response = getComps.getComps();

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
