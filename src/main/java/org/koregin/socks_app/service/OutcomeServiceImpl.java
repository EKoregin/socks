package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.entity.Employee;
import org.koregin.socks_app.database.entity.Outcome;
import org.koregin.socks_app.database.repository.EmployeeRepository;
import org.koregin.socks_app.database.repository.OutcomeRepository;
import org.koregin.socks_app.dto.OutcomeRequestDto;
import org.koregin.socks_app.mapper.OutcomeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OutcomeServiceImpl implements OutcomeService {

    private final OutcomeMapper outcomeMapper;
    private final OutcomeRepository outcomeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Long create(OutcomeRequestDto outcomeRequestDto) {
        Integer employeeId = outcomeRequestDto.getEmployeeId();
        log.info("Create outcome with employeeId: {}",employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new NoSuchElementException("User with id " + employeeId + " not found"));
        return Optional.of(outcomeRequestDto)
                .map(dto -> outcomeMapper.map(dto, employee))
                .map(outcomeRepository::save)
                .map(Outcome::getId)
                .orElseThrow();
    }
}
