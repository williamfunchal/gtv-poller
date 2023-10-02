package com.consensus.gtv.poller.models.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IspCorpProfileDTO {

    @CsvBindByName(column = "Op")
    private String op;

    @CsvBindByName(column = "RESELLERID")
    private String resellerId;

    @CsvBindByName(column = "APPLKEY")
    private String applKey;

    @CsvBindByName(column = "BUSINESSENTITY")
    private String businessEntity;

    @CsvBindByName(column = "INDUSTRYCATEGORY")
    private String industryCategory;

    @CsvBindByName(column = "YEARESTABLISHED")
    private String yearEstablished;

    @CsvBindByName(column = "NUMEMPLYEES")
    private String numEmplyees;

    @CsvBindByName(column = "YEARSATCURRENTLOCATION")
    private String yearsAtCurrentLocation;

    @CsvBindByName(column = "BUSINESSFACILITY")
    private String businessFacility;

    @CsvBindByName(column = "DUNANDBRADNUM")
    private String dunandbradnum;

    @CsvBindByName(column = "CREDITREQUESTED")
    private String creditRequested;

    @CsvBindByName(column = "SALESREP")
    private String salesRep;

    @CsvBindByName(column = "OFFERCODE")
    private String offerCode;

    @CsvBindByName(column = "USERDIDLIMIT")
    private String userDidLimit;

    @CsvBindByName(column = "USAGECREDITLIMIT")
    private String usageCreditLimit;

    @CsvBindByName(column = "PONUMBER")
    private String poNumber;

    @CsvBindByName(column = "CREATEDATE")
    private String createDate;

    @CsvBindByName(column = "LASTUPDATEDDATE")
    private String lastUpdateDate;

    @CsvBindByName(column = "STATUS")
    private String status;

    @CsvBindByName(column = "SECUREENABLE")
    private String secureEnable;

    @CsvBindByName(column = "SECURETYPE")
    private String secureType;

    @CsvBindByName(column = "INDUSTRYCODE")
    private String industryCode;

    @CsvBindByName(column = "TIERCODE")
    private String tierCode;

    @CsvBindByName(column = "LANDING_PAGE_MESSAGE")
    private String landingPageMessage;

    @CsvBindByName(column = "COMMISSION_FLAG")
    private String commissionFlag;

    @CsvBindByName(column = "PAYMENT_TERMS")
    private String paymentTerms;

    @CsvBindByName(column = "OCR")
    private String ocr;

    @CsvBindByName(column = "BAA")
    private String baa;

    @CsvBindByName(column = "COMPLIANCE_TYPE")
    private String complianceType;

    @CsvBindByName(column = "API")
    private String api;

    @CsvBindByName(column = "PSP")
    private String psp;

    @CsvBindByName(column = "PRODUCT_CODE")
    private String productCode;

    @CsvBindByName(column = "ROUTER")
    private String router;

    @CsvBindByName(column = "APITYPE")
    private String apiType;

    @CsvBindByName(column = "MARKETPLACE_CODE")
    private String marketplaceCode;

    @CsvBindByName(column = "MARKETPLACE_ACCOUNT_ID")
    private String marketplaceAccountId;

    @CsvBindByName(column = "MARKETPLACE_PRODUCT_ID")
    private String marketplaceProductId;

    @CsvBindByName(column = "MARKET_SEGMENT")
    private String marketSegment;

    @CsvBindByName(column = "SSO")
    private String sso;

    @CsvBindByName(column = "MARKETPLACE_PRODUCT_ID_2")
    private String marketplaceProductId2;

    @CsvBindByName(column = "SALESFORCE_CONNECTOR")
    private String salesforceConnector;

    @CsvBindByName(column = "MARKETPLACE_BILLING")
    private String marketplaceBilling;
}
