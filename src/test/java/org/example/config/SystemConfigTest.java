package org.example.config;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SystemConfigTest {

    @Test
    public void testGetSystemConfig() {
        SystemConfig systemConfig = new SystemConfig();

        String nin = "";
        String paramName = "Commission,prefixname";

        Response response = systemConfig.getSystemConfig(nin, paramName);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
