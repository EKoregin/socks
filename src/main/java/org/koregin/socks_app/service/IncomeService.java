package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.IncomeRequestDto;

public interface IncomeService {

    Long create(IncomeRequestDto incomeRequestDto);
}
