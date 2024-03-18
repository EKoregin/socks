package org.koregin.socks_app.service;

import org.apache.coyote.BadRequestException;
import org.koregin.socks_app.dto.IncomeCreateDto;
import org.koregin.socks_app.dto.SocksCreateDto;
import org.koregin.socks_app.dto.SocksIncomeCreateDto;
import org.koregin.socks_app.dto.SocksReadDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SocksService {
    Integer countByParams(String color, Integer cottonPart, String operation) throws BadRequestException;

    @Transactional
    SocksReadDto create(SocksCreateDto socksCreateDto);

    @Transactional
    Optional<SocksReadDto> update(Long id, SocksCreateDto socksCreateDto);

    @Transactional
    Long incomeCreate(IncomeCreateDto incomeCreateDto);

    @Transactional
    void addSocksToIncome(SocksIncomeCreateDto socksIncomeCreateDto);

    @Transactional
    boolean acceptIncome(Long incomeId);
}
