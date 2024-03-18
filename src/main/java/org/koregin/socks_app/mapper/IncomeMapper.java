package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.dto.IncomeCreateDto;
import org.koregin.socks_app.service.EmployeeService;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
//        uses = { EmployeeService.class }
)
public abstract class IncomeMapper {

//    @Mapping(source = "employeeId", target = "employee")
    public abstract Income map(IncomeCreateDto incomeCreateDto);
}
