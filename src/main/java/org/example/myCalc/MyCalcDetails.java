package org.example.myCalc;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class MyCalcDetails extends BaseApi {

    private static final String SERVICE_NAME = "MY_CALC_DETAILS";

    public MyCalc.Period getConfigPeriod() {
        return MyCalc.Period.fromString(config.getMyCalcPeriod());
    }

    public Response getMyCalcDetails(String nin, String dateFrom, String dateTo, MyCalc.Period period,
                                     String bal, String prevBal, String with, String depo,
                                     String cashDiv, String stkTransfer, String regTransfer,
                                     String fees, String lang, String lstLogin) {
        logger.info("Executing MyCalcDetails for NIN: {} with period: {}", nin, period.getValue());

        Map<String, Object> myCalcDetailsRequest = new HashMap<>();
        myCalcDetailsRequest.put("NIN", nin);
        myCalcDetailsRequest.put("dateFrom", dateFrom);
        myCalcDetailsRequest.put("dateTo", dateTo);
        myCalcDetailsRequest.put("period", period.getValue());
        myCalcDetailsRequest.put("bal", bal);
        myCalcDetailsRequest.put("prevBal", prevBal);
        myCalcDetailsRequest.put("with", with);
        myCalcDetailsRequest.put("depo", depo);
        myCalcDetailsRequest.put("cashDiv", cashDiv);
        myCalcDetailsRequest.put("stkTransfer", stkTransfer);
        myCalcDetailsRequest.put("regTransfer", regTransfer);
        myCalcDetailsRequest.put("fees", fees);

        Map<String, Object> message = new HashMap<>();
        message.put("MyCalcDetailsRequest", myCalcDetailsRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("Lang", lang);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getMyCalcDetails(String nin, String dateFrom, String dateTo, MyCalc.Period period,
                                     String bal, String prevBal, String with, String depo,
                                     String cashDiv, String stkTransfer, String regTransfer, String fees) {
        return getMyCalcDetails(nin, dateFrom, dateTo, period,
                bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees,
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getMyCalcDetails(String nin, MyCalc.Period period,
                                     String bal, String prevBal, String with, String depo,
                                     String cashDiv, String stkTransfer, String regTransfer, String fees) {
        String today = java.time.LocalDate.now().toString();
        return getMyCalcDetails(nin, today, today, period,
                bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees,
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getMyCalcDetails(String nin, String dateFrom, String dateTo,
                                     String bal, String prevBal, String with, String depo,
                                     String cashDiv, String stkTransfer, String regTransfer, String fees) {
        // Use period from config
        return getMyCalcDetails(nin, dateFrom, dateTo, getConfigPeriod(),
                bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees,
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getMyCalcDetails(String nin, String bal, String prevBal, String with, String depo,
                                     String cashDiv, String stkTransfer, String regTransfer, String fees) {
        // Use period from config and today's date
        String today = java.time.LocalDate.now().toString();
        return getMyCalcDetails(nin, today, today, getConfigPeriod(),
                bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees,
                config.getTestLanguage(), config.getTestLstLogin());
    }
}
