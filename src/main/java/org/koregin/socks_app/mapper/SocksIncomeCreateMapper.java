package org.koregin.socks_app.mapper;

import lombok.RequiredArgsConstructor;
import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.SocksIncome;
import org.koregin.socks_app.database.repository.IncomeRepository;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.SocksIncomeCreateDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SocksIncomeCreateMapper implements GenericMapper<SocksIncomeCreateDto, SocksIncome> {

    private final SocksRepository socksRepository;
    private final IncomeRepository incomeRepository;

    @Override
    public SocksIncome map(SocksIncomeCreateDto object) {
        SocksIncome socksIncome = new SocksIncome();
        socksIncome.setQuantity(object.getQuantity());
        socksIncome.setSocks(getSocks(object.getSocksId()));
        socksIncome.setIncome(getIncome(object.getIncomeId()));
        return socksIncome;
    }

    @Override
    public SocksIncome map(SocksIncomeCreateDto fromObject, SocksIncome toObject) {
        toObject.setQuantity(toObject.getQuantity() + fromObject.getQuantity());
        toObject.setSocks(getSocks(fromObject.getSocksId()));
        toObject.setIncome(getIncome(fromObject.getIncomeId()));
        return toObject;
    }

    private Socks getSocks(Long socksId) {
        return Optional.ofNullable(socksId)
                .flatMap(socksRepository::findById)
                .orElse(null);
    }

    private Income getIncome(Long incomeId) {
        return Optional.ofNullable(incomeId)
                .flatMap(incomeRepository::findById)
                .orElse(null);
    }
}
