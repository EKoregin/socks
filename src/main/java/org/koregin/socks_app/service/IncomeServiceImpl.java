package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.entity.Employee;
import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.repository.EmployeeRepository;
import org.koregin.socks_app.database.repository.IncomeRepository;
import org.koregin.socks_app.dto.IncomeRequestDto;
import org.koregin.socks_app.mapper.IncomeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

    private final IncomeMapper incomeMapper;
    private final IncomeRepository incomeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Long create(IncomeRequestDto incomeRequestDto) {
        Integer employeeId = incomeRequestDto.getEmployeeId();
        log.info("Create income with employeeId: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new NoSuchElementException("User with id " + employeeId + " not found"));
        return Optional.of(incomeRequestDto)
                .map(dto -> incomeMapper.map(dto, employee))
                .map(incomeRepository::save)
                .map(Income::getId)
                .orElseThrow();
    }
}
