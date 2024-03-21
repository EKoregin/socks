package org.koregin.socks_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koregin.socks_app.database.entity.Employee;
import org.koregin.socks_app.database.entity.Outcome;
import org.koregin.socks_app.database.repository.OutcomeRepository;
import org.koregin.socks_app.dto.OutcomeRequestDto;
import org.koregin.socks_app.mapper.OutcomeMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutcomeServiceImplTest {

    private static final Integer EMPLOYEE_ID = 99;
    private static final Long OUTCOME_ID = 99L;

    @Mock
    private OutcomeMapper outcomeMapper;

    @Mock
    private OutcomeRepository outcomeRepository;

    @InjectMocks
    private OutcomeServiceImpl outcomeService;

    @Test
    void create() {
        OutcomeRequestDto outcomeRequestDto = new OutcomeRequestDto(EMPLOYEE_ID);
        Outcome newOutcome = new Outcome(null, null, new Employee(), null);
        Outcome savedOutcome = new Outcome(OUTCOME_ID, null, new Employee(), null);

        when(outcomeMapper.map(outcomeRequestDto)).thenReturn(newOutcome);
        when(outcomeRepository.save(newOutcome)).thenReturn(savedOutcome);

        var actualResult = outcomeService.create(outcomeRequestDto);
        assertEquals(OUTCOME_ID, actualResult);
    }
}