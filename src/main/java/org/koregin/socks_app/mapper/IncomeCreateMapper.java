package org.koregin.socks_app.mapper;

import lombok.RequiredArgsConstructor;
import org.koregin.socks_app.database.entity.Employee;
import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.repository.EmployeeRepository;
import org.koregin.socks_app.dto.IncomeCreateDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncomeCreateMapper implements GenericMapper<IncomeCreateDto, Income>{

    private final EmployeeRepository employeeRepository;

    @Override
    public Income map(IncomeCreateDto object) {
        Income income = new Income();

        income.setCreated(LocalDateTime.now());
        income.setEmployee(getEmployee(object.getEmployeeId()));

        return income;
    }

    private Employee getEmployee(Integer employeeId) {
        return Optional.ofNullable(employeeId)
                .flatMap(employeeRepository::findById)
                .orElse(null);
    }
}
