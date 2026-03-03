package org.example.orders;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class SaveOrder extends BaseApi {

    private static final String SERVICE_NAME = "SAVE_ORDER";

    public Response saveOrder(String ordType, String nin, String sym, String shrsNo, String shrPrc,
                              String mkPrc, String mPayType, String ordVald, String expirationDateTime,
                              String usrId, String userType, String investorType, String lang,
                              String orderSource, String stopPrice, String fixType, String discloseVolume,
                              String clientAltIP, String clientUserIP, String lstLogin) {

        logger.info("Executing Save Order - Type: {}, Symbol: {}, Shares: {}", ordType, sym, shrsNo);

        Map<String, Object> saveOrdRequest = new HashMap<>();
        saveOrdRequest.put("ordType", ordType);
        saveOrdRequest.put("nin", nin);
        saveOrdRequest.put("sym", sym);
        saveOrdRequest.put("shrsNo", shrsNo);
        saveOrdRequest.put("shrPrc", shrPrc);
        saveOrdRequest.put("mkPrc", mkPrc);
        saveOrdRequest.put("mPay_type", mPayType);
        saveOrdRequest.put("ordVald", ordVald);
        saveOrdRequest.put("expirationDateTime", expirationDateTime);
        saveOrdRequest.put("usrId", usrId);
        saveOrdRequest.put("userType", userType);
        saveOrdRequest.put("investorType", investorType);
        saveOrdRequest.put("lang", lang);
        saveOrdRequest.put("orderSource", orderSource);
        saveOrdRequest.put("stopPrice", stopPrice);
        saveOrdRequest.put("fixType", fixType);
        saveOrdRequest.put("discloseVolume", discloseVolume);
        saveOrdRequest.put("clientAltIP", clientAltIP);
        saveOrdRequest.put("clientUserIP", clientUserIP);

        Map<String, Object> message = new HashMap<>();
        message.put("SaveOrdRequest", saveOrdRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("sessionId", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with default values
    public Response saveBuyOrder(String nin, String sym, String shrsNo, String shrPrc,
                                  String usrId, String userType, String fixType,
                                  String clientAltIP, String lstLogin) {
        return saveOrder(
                "BUY", nin, sym, shrsNo, shrPrc,
                config.getOrderMkPrc(), config.getOrderMPayType(), config.getOrderOrdVald(), "",
                usrId, userType, config.getDefaultInvestorType(), config.getTestLanguage(),
                APP_ID, "", fixType, config.getOrderDiscloseVolume(),
                clientAltIP, config.getClientUserIP(), lstLogin
        );
    }

    // Convenience method for sell orders
    public Response saveSellOrder(String nin, String sym, String shrsNo, String shrPrc,
                                   String usrId, String userType, String fixType,
                                   String clientAltIP, String lstLogin) {
        return saveOrder(
                "SELL", nin, sym, shrsNo, shrPrc,
                config.getOrderMkPrc(), config.getOrderMPayType(), config.getOrderOrdVald(), "",
                usrId, userType, config.getDefaultInvestorType(), config.getTestLanguage(),
                APP_ID, "", fixType, config.getOrderDiscloseVolume(),
                clientAltIP, config.getClientUserIP(), lstLogin
        );
    }
}
