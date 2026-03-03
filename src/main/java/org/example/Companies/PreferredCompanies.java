package org.example.Companies;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class PreferredCompanies extends BaseApi {

    private static final String SERVICE_NAME = "PREFERRED_COMPANIES";

    public Response getPreferredCompanies(String nin, String lstLogin) {
        logger.info("Executing PreferredCompanies for NIN: {}", nin);

        Map<String, Object> companiesRequest = new HashMap<>();
        companiesRequest.put("NIN", nin);

        Map<String, Object> message = new HashMap<>();
        message.put("CompaniesRequest", companiesRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getPreferredCompanies(String nin) {
        return getPreferredCompanies(nin, config.getTestLstLogin());
    }
}
