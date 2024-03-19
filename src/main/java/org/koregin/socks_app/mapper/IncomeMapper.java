package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.repository.EmployeeRepository;
import org.koregin.socks_app.dto.IncomeCreateDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class}
)
public abstract class IncomeMapper {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Mapping(target = "employee", expression = "java(employeeRepository.findById(dto.getEmployeeId()).orElse(null))")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    public abstract Income map(IncomeCreateDto dto);
}
