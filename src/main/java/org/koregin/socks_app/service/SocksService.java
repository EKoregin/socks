package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.SocksCreateDto;
import org.koregin.socks_app.dto.SocksReadDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SocksService {

    @Transactional
    SocksReadDto create(SocksCreateDto socksCreateDto);

    @Transactional
    Optional<SocksReadDto> update(Long id, SocksCreateDto socksCreateDto);

}
