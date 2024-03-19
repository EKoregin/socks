package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.SocksCreateDto;
import org.koregin.socks_app.dto.SocksReadDto;
import org.koregin.socks_app.mapper.SocksMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SocksReadDto create(SocksCreateDto socksDto) {
        log.info("Socks create method start...");
        return Optional.of(socksDto)
                .map(socksMapper::dtoToSocks)
                .map(socksRepository::save)
                .map(socksMapper::socksToDto)
                .orElseThrow();
    }

    @Override
    public Optional<SocksReadDto> update(Long id, SocksCreateDto socksCreateDto) {
        log.info("Update socks with id: {}", id);
        return socksRepository.findById(id)
                .map(entity -> socksMapper.update(socksCreateDto, entity))
                .map(socksRepository::saveAndFlush)
                .map(socksMapper::socksToDto);
    }
}
