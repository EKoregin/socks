package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.dto.WarehouseRequestDto;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WarehouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "socks", source = "socks")
    Warehouse dtoToWarehouse(WarehouseRequestDto dto, Socks socks);
}
