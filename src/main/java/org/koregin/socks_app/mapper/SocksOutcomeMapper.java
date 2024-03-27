package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Outcome;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.SocksOutcome;
import org.koregin.socks_app.dto.SocksOutcomeRequestDto;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SocksOutcomeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "socks", source = "socks")
    @Mapping(target = "outcome", source = "outcome")
    SocksOutcome dtoToSocksOutcome(SocksOutcomeRequestDto dto, Socks socks, Outcome outcome);
}
