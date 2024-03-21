package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.SocksRequestDto;
import org.koregin.socks_app.dto.SocksResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface SocksService {

    @Transactional
    SocksResponseDto create(SocksRequestDto socksRequestDto);

    @Transactional
    SocksResponseDto update(Long id, SocksRequestDto socksRequestDto);

}
