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
public class IspServiceDTO {

    @CsvBindByName(column = "Op")
    private String op;

    @CsvBindByName(column = "SERVICEKEY")
    private String serviceKey;

    @CsvBindByName(column = "SERVICEID")
    private String serviceId;

    @CsvBindByName(column = "CUSTOMERKEY")
    private String customerKey;

    @CsvBindByName(column = "SERVICETYPE")
    private String serviceType;

    @CsvBindByName(column = "ACCOUNTFLAG")
    private String accountFlag;

    @CsvBindByName(column = "STATUS")
    private String status;

    @CsvBindByName(column = "STARTDATE")
    private String startDate;

    @CsvBindByName(column = "ENDDATE")
    private String endDate;

    @CsvBindByName(column = "LASTCHARGEDATE")
    private String lastChargeDate;

    @CsvBindByName(column = "LASTCHARGEDAY")
    private String lastChargeDay;

    @CsvBindByName(column = "NEXTCHARGEDATE")
    private String nextChargeDate;

    @CsvBindByName(column = "NEXTCHARGEDAY")
    private String nextChargeDay;

    @CsvBindByName(column = "PREPAIDCHARGEDATE")
    private String prepaidChargeDate;

    @CsvBindByName(column = "PREPAIDCHARGEDAY")
    private String prepaidChargeDay;

    @CsvBindByName(column = "PREPAIDCREDIT")
    private String prepaidCredit;

    @CsvBindByName(column = "PREPAIDJOURNALKEY")
    private String prepaidJournalKey;

    @CsvBindByName(column = "FREEOPENFLAG")
    private String freeOpenFlag;

    @CsvBindByName(column = "FREECLOSEFLAG")
    private String freeCloseFlag;

    @CsvBindByName(column = "SWITCHOPERATION")
    private String switchOperation;

    @CsvBindByName(column = "SWITCHSERVICETYPE")
    private String switchServiceType;

    @CsvBindByName(column = "SWITCHCHARGEFLAG")
    private String switchChargeFlag;

    @CsvBindByName(column = "ORIGINALSERVICEKEY")
    private String originalServiceKey;

    @CsvBindByName(column = "DISCOUNTRATE")
    private String discountRate;

    @CsvBindByName(column = "DISCOUNTACCOUNTCODE")
    private String discountAccountCode;

    @CsvBindByName(column = "LOGIN")
    private String login;

    @CsvBindByName(column = "PASSWORD")
    private String password;

    @CsvBindByName(column = "NAME")
    private String name;

    @CsvBindByName(column = "NOTES")
    private String notes;

    @CsvBindByName(column = "EMAILTOSEND")
    private String emailToSend;

    @CsvBindByName(column = "SENDSTATUS")
    private String sendStatus;

    @CsvBindByName(column = "EMAILTOSENDBUNDLE")
    private String emailToSendBundle;

    @CsvBindByName(column = "PIN")
    private String pin;

    @CsvBindByName(column = "OFFER_CODE")
    private String offerCode;

    @CsvBindByName(column = "OFFER_CODE_STARTDATE")
    private String offerCodeStartDate;

    @CsvBindByName(column = "PRODUCT_TYPE")
    private String productType;

    @CsvBindByName(column = "RESOURCE_TYPE")
    private String resourceType;

    @CsvBindByName(column = "WEBACCESSSTATUS")
    private String webAccessStatus;

    @CsvBindByName(column = "CANCELCODE")
    private String cancelCode;

    @CsvBindByName(column = "SIG")
    private String sig;

    @CsvBindByName(column = "UNUSEDFREEDAYS")
    private String unusedFreeDays;

    @CsvBindByName(column = "USAGECOUNT")
    private String usageCount;

    @CsvBindByName(column = "LASTPERIODICCHARGEDATE")
    private String lastPeriodicChargeDate;

    @CsvBindByName(column = "NEXTPERIODICCHARGEDATE")
    private String nextPeriodicChargeDate;

    @CsvBindByName(column = "ADDCOLLECTIONFLAG")
    private String addCollenctionFlag;

    @CsvBindByName(column = "BILLINGPERIODSTARTDATE")
    private String billingPeriodStartdate;

    @CsvBindByName(column = "AUTOLOGINHASH")
    private String autoLoginHash;

    @CsvBindByName(column = "CREATEUSERID")
    private String createUserId;

    @CsvBindByName(column = "CREATEDATETIME")
    private String createDateTime;

    @CsvBindByName(column = "MODIFYUSERID")
    private String modifyUserId;

    @CsvBindByName(column = "MODIFYORACLEUSER")
    private String modifyOracleUser;

    @CsvBindByName(column = "MODIFYORACLETERMINAL")
    private String modifyOracleTerminal;

    @CsvBindByName(column = "MODIFYDATETIME")
    private String modifyDateTime;

    @CsvBindByName(column = "SALESUSERID")
    private String salesUserId;

    @CsvBindByName(column = "STARTCODE")
    private String startCode;

    @CsvBindByName(column = "REF_CODE")
    private String refCode;

    @CsvBindByName(column = "RESELLERID")
    private String resellerId;

    @CsvBindByName(column = "ENTRYLOCATION")
    private String entryLocation;

    @CsvBindByName(column = "CREATIVE_KEY")
    private String creativeKey;

    @CsvBindByName(column = "PRIORSERVICEKEY")
    private String priorServiceKey;

    @CsvBindByName(column = "CAMPAIGN_ID")
    private String campaingId;

    @CsvBindByName(column = "PROMOTION_CODE")
    private String promotionCode;

    @CsvBindByName(column = "TRACKING_DATA")
    private String tarckingData;

    @CsvBindByName(column = "NO_ACT_FLAG")
    private String noActFlag;

    @CsvBindByName(column = "FREE_TRIAL_FLAG")
    private String freeTrialFlag;

    @CsvBindByName(column = "BILLING_PERIOD")
    private String billingPeriod;

    @CsvBindByName(column = "VOICE_MAIL_FLAG")
    private String voiceMailFlag;

    @CsvBindByName(column = "VERSION")
    private String version;

    @CsvBindByName(column = "SOURCE_APP")
    private String sourceApp;

    @CsvBindByName(column = "FREETRIALENDDATE")
    private String freeTrialEndDate;

    @CsvBindByName(column = "PIN_HASH")
    private String pinHash;

}
