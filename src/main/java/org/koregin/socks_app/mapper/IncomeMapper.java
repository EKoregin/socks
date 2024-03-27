package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Employee;
import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.dto.IncomeRequestDto;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class}
)
public interface IncomeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Income map(IncomeRequestDto dto, Employee employee);
}
