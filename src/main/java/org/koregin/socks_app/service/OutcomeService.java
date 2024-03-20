package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.OutcomeRequestDto;

public interface OutcomeService {

    Long create(OutcomeRequestDto outcomeRequestDto);
}
