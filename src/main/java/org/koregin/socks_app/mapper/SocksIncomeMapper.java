package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.SocksIncome;
import org.koregin.socks_app.database.repository.IncomeRepository;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.SocksIncomeRequestDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SocksIncomeMapper {

    @Autowired
    protected SocksRepository socksRepository;

    @Autowired
    protected IncomeRepository incomeRepository;

    @Mapping(target = "socks", expression = "java(socksRepository.findById(dto.getSocksId()).orElse(null))")
    @Mapping(target = "income", expression = "java(incomeRepository.findById(dto.getIncomeId()).orElse(null))")
    public abstract SocksIncome dtoToSocksIncome(SocksIncomeRequestDto dto);

    @Mapping(target = "socks", expression = "java(socksRepository.findById(dto.getSocksId()).orElse(null))")
    @Mapping(target = "income", expression = "java(incomeRepository.findById(dto.getIncomeId()).orElse(null))")
    public abstract SocksIncome update(SocksIncomeRequestDto dto, @MappingTarget SocksIncome socksIncome);
}
