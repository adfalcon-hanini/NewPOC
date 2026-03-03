package org.example.alerts;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class UpdateAlert extends BaseApi {

    private static final String SERVICE_NAME = "MNGALERT";
    private static final String REQ_OP_CODE = "UPDATE";

    public Response updateAlert(String alertId, String alertTitle, String symbolId,
                                String interestId, String criteriaId, String currentValue,
                                String alertValue, String valueFrom, String valueTo,
                                String dateFrom, String dateTo, String alertType,
                                String singleDate, String nin, String alertSource,
                                String lang, String userId, String userType, String lstLogin) {
        logger.info("Executing UpdateAlert for AlertId: {}, NIN: {}", alertId, nin);

        Map<String, Object> alertRequest = new HashMap<>();
        alertRequest.put("ReqAlertId", alertId);
        alertRequest.put("ReqAlertTitle", alertTitle);
        alertRequest.put("ReqAlertSymbolId", symbolId);
        alertRequest.put("ReqAlertInterestId", interestId);
        alertRequest.put("ReqAlertCriteriaId", criteriaId);
        alertRequest.put("ReqAlertCurrentValue", currentValue);
        alertRequest.put("ReqAlertValue", alertValue);
        alertRequest.put("ReqValueFrom", valueFrom);
        alertRequest.put("ReqValueTo", valueTo);
        alertRequest.put("ReqDateFrom", dateFrom);
        alertRequest.put("ReqDateTo", dateTo);
        alertRequest.put("ReqAlertType", alertType);
        alertRequest.put("ReqAlertSingleDate", singleDate);
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

    // Convenience method with common defaults
    public Response updateAlert(String alertId, String alertTitle, String symbolId,
                                String interestId, String currentValue, String alertValue,
                                String nin, String lstLogin) {
        return updateAlert(
                alertId,
                alertTitle,
                symbolId,
                interestId,
                "",
                currentValue,
                alertValue,
                "",
                "",
                "",
                "",
                "",
                config.getAlertSingleDate(),
                nin,
                "",
                config.getTestLanguage(),
                nin,
                config.getAlertUserType(),
                lstLogin
        );
    }

    // Convenience method using config values
    public Response updateAlert(String alertId, String alertTitle, String symbolId,
                                String interestId, String currentValue, String alertValue,
                                String nin) {
        return updateAlert(
                alertId,
                alertTitle,
                symbolId,
                interestId,
                currentValue,
                alertValue,
                nin,
                config.getTestLstLogin()
        );
    }
}
