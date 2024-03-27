package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.SocksIncome;
import org.koregin.socks_app.dto.SocksIncomeRequestDto;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SocksIncomeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "socks", source = "socks")
    @Mapping(target = "income", source = "income")
    SocksIncome dtoToSocksIncome(SocksIncomeRequestDto dto, Socks socks, Income income);
}
