package com.consensus.gtv.poller.models.mapper;

import com.consensus.gtv.poller.models.dto.IspCorpProfileDTO;
import com.consensus.gtv.poller.models.rawdata.IspCorpProfileData;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IspCorpProfileMapper {

    IspCorpProfileData toCorpProfileData(IspCorpProfileDTO ispCorpProfileDTO);
}
