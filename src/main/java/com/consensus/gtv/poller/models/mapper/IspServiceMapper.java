package com.consensus.gtv.poller.models.mapper;

import com.consensus.gtv.poller.models.dto.IspServiceDTO;
import com.consensus.gtv.poller.models.rawdata.IspServiceData;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IspServiceMapper {

    IspServiceData toIspServiceData(IspServiceDTO ispServiceDTO);
}
