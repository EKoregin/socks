package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.WarehouseCreateDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class WarehouseMapper {

    @Autowired
    protected SocksRepository socksRepository;

    @Mapping(target = "socks", expression = "java(socksRepository.findById(dto.getSocksId()).orElse(null))")
    public abstract Warehouse dtoToWarehouse(WarehouseCreateDto dto);

    @Mapping(target = "socks", expression = "java(socksRepository.findById(dto.getSocksId()).orElse(null))")
    public abstract Warehouse update(WarehouseCreateDto dto, @MappingTarget Warehouse warehouse);
}
