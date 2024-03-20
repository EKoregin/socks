package org.koregin.socks_app.mapper;

import org.koregin.socks_app.database.entity.SocksOutcome;
import org.koregin.socks_app.database.repository.OutcomeRepository;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.SocksOutcomeRequestDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SocksOutcomeMapper {

    @Autowired
    protected SocksRepository socksRepository;

    @Autowired
    protected OutcomeRepository outcomeRepository;

    @Mapping(target = "socks", expression = "java(socksRepository.findById(dto.getSocksId()).orElse(null))")
    @Mapping(target = "outcome", expression = "java(outcomeRepository.findById(dto.getOutcomeId()).orElse(null))")
    public abstract SocksOutcome dtoToSocksOutcome(SocksOutcomeRequestDto dto);

    @Mapping(target = "socks", expression = "java(socksRepository.findById(dto.getSocksId()).orElse(null))")
    @Mapping(target = "outcome", expression = "java(outcomeRepository.findById(dto.getOutcomeId()).orElse(null))")
    public abstract SocksOutcome update(SocksOutcomeRequestDto dto, @MappingTarget SocksOutcome socksOutcome);
}
