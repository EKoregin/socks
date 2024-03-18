package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.dto.SocksCreateDto;
import org.koregin.socks_app.dto.SocksReadDto;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SocksMapper {

    public abstract Socks dtoToSocks(SocksCreateDto socksCreateDto);
    public abstract SocksReadDto socksToDto(Socks socks);

    public abstract Socks update(SocksCreateDto dto, @MappingTarget Socks socks);
}
