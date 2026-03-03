package org.example.alerts;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class RemoveAlert extends BaseApi {

    private static final String SERVICE_NAME = "MNGALERT";
    private static final String REQ_OP_CODE = "REMOVE";

    public Response removeAlert(String alertId, String nin, String alertSource,
                                String lang, String userId, String userType, String lstLogin) {
        logger.info("Executing RemoveAlert for AlertId: {}, NIN: {}", alertId, nin);

        Map<String, Object> alertRequest = new HashMap<>();
        alertRequest.put("ReqAlertId", alertId);
        alertRequest.put("ReqOpCode", REQ_OP_CODE);
        alertRequest.put("ReqAlertNin", nin);
        alertRequest.put("ReqAlertSource", alertSource);
        alertRequest.put("Lang", lang);
        alertRequest.put("UserId", userId);
        alertRequest.put("UserType", userType);

        Map<String, Object> message = new HashMap<>();
        message.put("AlertRequest", alertRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("SessionID", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with defaults
    public Response removeAlert(String alertId, String nin, String lstLogin) {
        return removeAlert(
                alertId,
                nin,
                "",
                config.getTestLanguage(),
                nin,
                config.getAlertUserType(),
                lstLogin
        );
    }

    // Convenience method using config values
    public Response removeAlert(String alertId, String nin) {
        return removeAlert(alertId, nin, config.getTestLstLogin());
    }
}
