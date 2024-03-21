package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.SocksRequestDto;
import org.koregin.socks_app.dto.SocksResponseDto;
import org.koregin.socks_app.mapper.SocksMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final SocksMapper socksMapper;

    @Override
    @Transactional
    public SocksResponseDto create(SocksRequestDto socksDto) {
        log.info("Socks create method start...");
        return Optional.of(socksDto)
                .map(socksMapper::dtoToSocks)
                .map(socksRepository::save)
                .map(socksMapper::socksToDto)
                .orElseThrow();
    }

    @Override
    public SocksResponseDto update(Long id, SocksRequestDto socksRequestDto) {
        log.info("Update socks with id: {}", id);
        return socksRepository.findById(id)
                .map(entity -> socksMapper.update(socksRequestDto, entity))
                .map(socksRepository::saveAndFlush)
                .map(socksMapper::socksToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
