package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.dto.SocksRequestDto;
import org.koregin.socks_app.dto.SocksResponseDto;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SocksMapper {

    public abstract Socks dtoToSocks(SocksRequestDto socksRequestDto);
    public abstract SocksResponseDto socksToDto(Socks socks);

    public abstract Socks update(SocksRequestDto dto, @MappingTarget Socks socks);
}
