package com.consensus.gtv.poller.models.mapper;

import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtv.poller.models.dto.IspS3CustomerDTO;
import com.consensus.gtv.poller.models.sqs.IspNewCustomerEvent;
import com.consensus.gtv.poller.models.rawdata.DataOperation;
import com.consensus.gtv.poller.models.rawdata.IspCustomerData;
import org.springframework.stereotype.Component;

@Component
public class IspCustomerMapper {

    public IspNewCustomerEvent toNewCustomerEvent(IspS3CustomerDTO ispS3CustomerDTO) {
        IspNewCustomerEvent ispNewCustomerEvent = new IspNewCustomerEvent();
        ispNewCustomerEvent.setTableName("ISPCUSTOMER");
        ispNewCustomerEvent.setOperation(DataOperation.fromValue(ispS3CustomerDTO.getOp()));
        ispNewCustomerEvent.setData(IspCustomerData.builder()
                .customerkey(ispS3CustomerDTO.getCustomerkey())
                .company(ispS3CustomerDTO.getCompany())
                .country(ispS3CustomerDTO.getCountry())
                .addressLine1(ispS3CustomerDTO.getAddressLine1())
                .addressLine2(ispS3CustomerDTO.getAddressLine2())
                .city(ispS3CustomerDTO.getCity())
                .mailRegion(ispS3CustomerDTO.getMailRegion())
                .mailCode(ispS3CustomerDTO.getMailCode())
                .emailAddress(ispS3CustomerDTO.getEmailAddress())
                .startDate(ispS3CustomerDTO.getStartDate())
                .currencyCode(ispS3CustomerDTO.getCurrencyCode())
                //.paymentTerms(ispS3CustomerDTO.getPaymentTerms()) TODO - check if this is needed
                .resellerId(ispS3CustomerDTO.getResellerId())
                .offerCode(ispS3CustomerDTO.getOfferCode())
                .build());

        ispNewCustomerEvent.setEventId(CCSIUUIDUtils.generateUUID());
        ispNewCustomerEvent.setCorrelationId(CCSIUUIDUtils.generateUUID());

        return ispNewCustomerEvent;
    }
}
