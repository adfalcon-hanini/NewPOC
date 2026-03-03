package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading config.properties: {}", e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // API Configuration
    public String getBaseUrl() {
        return getProperty("api.base.url");
    }

    public String getVersion() {
        return getProperty("api.version", "1.0");
    }

    public String getAppId() {
        return getProperty("api.app.id", "WEB_ORDERS.25");
    }

    // Test Credentials
    public String getTestNin() {
        return getProperty("test.nin");
    }

    public String getTestPassword() {
        return getProperty("test.password");
    }

    public String getTestLanguage() {
        return getProperty("test.language", "ARB");
    }

    public String getTestLstLogin() {
        return getProperty("test.lstLogin", "08-07-2015");
    }

    // Bank Account Registration
    public String getBankAccountNo() {
        return getProperty("bank.accountNo");
    }

    public String getBankSwiftAddress() {
        return getProperty("bank.swiftAddress");
    }

    public String getBankCurrencyCode() {
        return getProperty("bank.currencyCode");
    }

    public String getBankAccountIBAN() {
        return getProperty("bank.accountIBAN");
    }

    // MyCalc Configuration
    public String getMyCalcPeriod() {
        return getProperty("mycalc.period", "ALL").trim();
    }

    public String getMyCalcDateFrom() {
        return getProperty("mycalc.dateFrom", java.time.LocalDate.now().toString());
    }

    public String getMyCalcDateTo() {
        return getProperty("mycalc.dateTo", java.time.LocalDate.now().toString());
    }

    public String getMyCalcBal() {
        return getProperty("mycalc.bal", "0");
    }

    public String getMyCalcPrevBal() {
        return getProperty("mycalc.prevBal", "0");
    }

    public String getMyCalcWith() {
        return getProperty("mycalc.with", "0");
    }

    public String getMyCalcDepo() {
        return getProperty("mycalc.depo", "0");
    }

    public String getMyCalcCashDiv() {
        return getProperty("mycalc.cashDiv", "0");
    }

    public String getMyCalcStkTransfer() {
        return getProperty("mycalc.stkTransfer", "0");
    }

    public String getMyCalcRegTransfer() {
        return getProperty("mycalc.regTransfer", "0");
    }

    public String getMyCalcFees() {
        return getProperty("mycalc.fees", "0");
    }

    // Cash Distribution Configuration
    public boolean getWithCashDistribution() {
        return Boolean.parseBoolean(getProperty("cashDistribution.withCashDistribution", "true"));
    }

    // Order Configuration
    public String getOrderOrdType() {
        return getProperty("order.ordType", "BUY");
    }

    public String getOrderSymbol() {
        return getProperty("order.symbol", "QNBK");
    }

    public String getOrderSharesNo() {
        return getProperty("order.sharesNo", "100");
    }

    public String getOrderSharePrice() {
        return getProperty("order.sharePrice", "15.50");
    }

    public String getOrderSharePriceMarket() {
        return getProperty("order.sharePriceMarket", "17");
    }

    public String getOrderUserType() {
        return getProperty("order.userType", "1");
    }

    public String getOrderFixType() {
        return getProperty("order.fixType", "1");
    }

    public String getOrderInvestorType() {
        return getProperty("order.investorType", "INVESTOR");
    }

    public String getOrderMkPrc() {
        return getProperty("order.mkPrc", "N");
    }

    public String getOrderMPayType() {
        return getProperty("order.mPayType", "KB");
    }

    public String getOrderOrdVald() {
        return getProperty("order.ordVald", "1");
    }

    public String getOrderDiscloseVolume() {
        return getProperty("order.discloseVolume", "0");
    }

    public String getOrderClientAltIP() {
        return getProperty("order.clientAltIP", "10.80.81.158");
    }

    public String getOrderClientUserIP() {
        return getProperty("order.clientUserIP", "10.80.81.158");
    }

    // Database Configuration
    public String getDbUrl() {
        return getProperty("db.url");
    }

    public String getDbUsername() {
        return getProperty("db.username");
    }

    public String getDbPassword() {
        return getProperty("db.password");
    }

    public String getDbDriver() {
        return getProperty("db.driver", "oracle.jdbc.OracleDriver");
    }

    // Common User Configuration
    public String getDefaultUserType() {
        return getProperty("user.type", "INVESTOR");
    }

    public String getDefaultInvestorType() {
        return getProperty("user.investorType", "INVESTOR");
    }

    // Banking Configuration
    public String getBankingUserType() {
        return getProperty("banking.userType", "INVESTOR");
    }

    public String getBankingAccRegisterType() {
        return getProperty("banking.accRegisterType", "ALL");
    }

    public String getBankingRemoveStatus() {
        return getProperty("banking.removeStatus", "2");
    }

    public String getBankingSwiftCode() {
        return getProperty("banking.swiftCode", "QISBQAQA");
    }

    public String getBankingTransferAmount() {
        return getProperty("banking.transfer.amount", "100");
    }

    // Alert Configuration
    public String getAlertUserType() {
        return getProperty("alert.userType", "INVESTOR");
    }

    public String getAlertSingleDate() {
        return getProperty("alert.singleDate", "Y");
    }

    public String getAlertSymbolId() {
        return getProperty("alert.symbolId", "10");
    }

    public String getAlertInterestId() {
        return getProperty("alert.interestId", "62");
    }

    public String getAlertCurrentValue() {
        return getProperty("alert.currentValue", "10");
    }

    public String getAlertValue() {
        return getProperty("alert.value", "11");
    }

    public String getAlertDateFrom() {
        return getProperty("alert.dateFrom", "2");
    }

    public String getAlertType() {
        return getProperty("alert.type", "CMP");
    }

    // Client IP Configuration
    public String getClientAltIP() {
        return getProperty("client.altIP", "10.80.81.158");
    }

    public String getClientUserIP() {
        return getProperty("client.userIP", "10.80.81.158");
    }

    // Update MyCalc properties in config.properties file
    public void updateMyCalcProperties(String bal, String prevBal, String with, String depo,
                                        String cashDiv, String stkTransfer, String regTransfer, String fees) {
        String configFilePath = "src/test/resources/config.properties";

        try {
            // Read the file content
            String content = new String(Files.readAllBytes(Paths.get(configFilePath)));

            // Update each property value
            content = content.replaceAll("mycalc.bal=.*", "mycalc.bal=" + bal);
            content = content.replaceAll("mycalc.prevBal=.*", "mycalc.prevBal=" + prevBal);
            content = content.replaceAll("mycalc.with=.*", "mycalc.with=" + with);
            content = content.replaceAll("mycalc.depo=.*", "mycalc.depo=" + depo);
            content = content.replaceAll("mycalc.cashDiv=.*", "mycalc.cashDiv=" + cashDiv);
            content = content.replaceAll("mycalc.stkTransfer=.*", "mycalc.stkTransfer=" + stkTransfer);
            content = content.replaceAll("mycalc.regTransfer=.*", "mycalc.regTransfer=" + regTransfer);
            content = content.replaceAll("mycalc.fees=.*", "mycalc.fees=" + fees);

            // Write the updated content back to file
            Files.write(Paths.get(configFilePath), content.getBytes());

            // Update in-memory properties
            properties.setProperty("mycalc.bal", bal);
            properties.setProperty("mycalc.prevBal", prevBal);
            properties.setProperty("mycalc.with", with);
            properties.setProperty("mycalc.depo", depo);
            properties.setProperty("mycalc.cashDiv", cashDiv);
            properties.setProperty("mycalc.stkTransfer", stkTransfer);
            properties.setProperty("mycalc.regTransfer", regTransfer);
            properties.setProperty("mycalc.fees", fees);

            logger.info("MyCalc properties updated in config.properties - bal: {}, prevBal: {}, with: {}, depo: {}, cashDiv: {}, stkTransfer: {}, regTransfer: {}, fees: {}",
                    bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees);
        } catch (IOException e) {
            logger.error("Failed to update config.properties: {}", e.getMessage());
        }
    }
}
