package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.entity.Outcome;
import org.koregin.socks_app.database.repository.OutcomeRepository;
import org.koregin.socks_app.dto.OutcomeRequestDto;
import org.koregin.socks_app.mapper.OutcomeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OutcomeServiceImpl implements OutcomeService {

    private final OutcomeMapper outcomeMapper;
    private final OutcomeRepository outcomeRepository;

    @Override
    public Long create(OutcomeRequestDto outcomeRequestDto) {
        log.info("Create outcome with employeeId: {}", outcomeRequestDto.getEmployeeId());
        return Optional.of(outcomeRequestDto)
                .map(outcomeMapper::map)
                .map(outcomeRepository::save)
                .map(Outcome::getId)
                .orElseThrow();
    }
}
