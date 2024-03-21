package org.koregin.socks_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koregin.socks_app.database.entity.Employee;
import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.repository.IncomeRepository;
import org.koregin.socks_app.dto.IncomeRequestDto;
import org.koregin.socks_app.mapper.IncomeMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncomeServiceImplTest {

    private static final Integer EMPLOYEE_ID = 99;
    private static final Long INCOME_ID = 99L;

    @Mock
    private IncomeMapper incomeMapper;

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    @Test
    void create() {
        IncomeRequestDto incomeRequestDto = new IncomeRequestDto(EMPLOYEE_ID);
        Income newIncome = new Income(null, null, new Employee(), null);
        Income savedIncome = new Income(INCOME_ID, null, new Employee(), null);

        when(incomeMapper.map(incomeRequestDto)).thenReturn(newIncome);
        when(incomeRepository.save(newIncome)).thenReturn(savedIncome);

        var actualResult = incomeService.create(incomeRequestDto);
        assertEquals(INCOME_ID, actualResult);
    }

}