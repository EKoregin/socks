package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.IncomeCreateDto;

public interface IncomeService {

    Long create(IncomeCreateDto incomeCreateDto);
}
