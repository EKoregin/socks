package org.koregin.socks_app.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.SocksIncome;
import org.koregin.socks_app.database.entity.SocksOutcome;
import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.database.repository.*;
import org.koregin.socks_app.dto.*;
import org.koregin.socks_app.exceptions.NotCorrectOperationException;
import org.koregin.socks_app.mapper.SocksIncomeMapper;
import org.koregin.socks_app.mapper.SocksOutcomeMapper;
import org.koregin.socks_app.service.IncomeService;
import org.koregin.socks_app.service.OutcomeService;
import org.koregin.socks_app.service.WarehouseService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class SocksFacade {

    private final IncomeService incomeService;
    private final IncomeRepository incomeRepository;
    private final SocksIncomeRepository socksIncomeRepository;
    private final SocksRepository socksRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;
    private final SocksIncomeMapper socksIncomeMapper;
    private final OutcomeService outcomeService;
    private final OutcomeRepository outcomeRepository;
    private final SocksOutcomeRepository socksOutcomeRepository;
    private final SocksOutcomeMapper socksOutcomeMapper;

    public Integer countByParams(String color, Integer cottonPart, String operation) {
        checkOperation(operation);

        if ("moreThan".equals(operation)) {
            log.info("More than: " + cottonPart);
            return getSocksCountFromWarehouse(socksRepository.findAllByColorAndCottonPartAfter(color, cottonPart));
        } else if ("equal".equals(operation)) {
            return getSocksCountFromWarehouse(socksRepository.findAllByColorAndCottonPart(color, cottonPart));
        }
        return getSocksCountFromWarehouse(socksRepository.findAllByColorAndCottonPartBefore(color, cottonPart));
    }

    private Integer getSocksCountFromWarehouse(List<Socks> socksList) {
        return socksList.stream()
                .map(socks -> {
                    var total = warehouseRepository.findBySocks(socks)
                            .map(Warehouse::getTotal).orElse(0);
                    log.info("Total socks: {}, {}", socks.getName(), total);
                    return total;
                })
                .reduce(0, Integer::sum);
    }

    private void checkOperation(String operation) {
        if (!"moreThan".equals(operation) && !"lessThan".equals(operation) && !"equal".equals(operation)) {
            throw new NotCorrectOperationException("Not correct operation parameter: " + operation);
        }
    }

    public Long createIncome(IncomeCreateDto incomeCreateDto) {
        return incomeService.create(incomeCreateDto);
    }

    public void addSocksToIncome(SocksIncomeCreateDto socksIncomeCreateDto) {
        var socksId = socksIncomeCreateDto.getSocksId();
        var income = incomeRepository.findById(socksIncomeCreateDto.getIncomeId()).orElseThrow();
        var socks = socksRepository.findById(socksIncomeCreateDto.getSocksId()).orElseThrow();
        var foundSocksIncome = socksIncomeRepository.findByIncomeAndSocks(income, socks);

        if (foundSocksIncome.isPresent()) {
            SocksIncome socksIncome = socksIncomeMapper.update(socksIncomeCreateDto, foundSocksIncome.get());
            log.info("Socks income for income with ID: {} and socksId: {}, was updated, new quantity: {}",
                    socksIncomeCreateDto.getIncomeId(), socksId, socksIncomeCreateDto.getQuantity());
            socksIncomeRepository.saveAndFlush(socksIncome);
        } else {
            log.info("Add new socks with ID: {} to income with ID: {}", socksId, socksIncomeCreateDto.getIncomeId());
            SocksIncome socksIncome = socksIncomeMapper.dtoToSocksIncome(socksIncomeCreateDto);
            socksIncomeRepository.save(socksIncome);
        }
    }

    public boolean acceptIncome(Long incomeId) {
        var foundIncome = incomeRepository.findById(incomeId);
        if (foundIncome.isPresent()) {
            var socksList = foundIncome.get().getItems();
            for (SocksIncome socksIncome : socksList) {
                var warehouseCreateDto = new WarehouseCreateDto(socksIncome.getQuantity(), socksIncome.getSocks().getId());
                warehouseService.addSocksToWarehouse(warehouseCreateDto);
            }
            log.info("Income with ID: {} was accepted in warehouse", incomeId);
            return true;
        } else {
            log.info("Income with ID: {} not found", incomeId);
            return false;
        }
    }

    public Long createOutcome(OutcomeRequestDto outcomeRequestDto) {
        return outcomeService.create(outcomeRequestDto);
    }

    public void addSocksToOutcome(SocksOutcomeRequestDto socksOutcomeRequestDto) {
        var socksId = socksOutcomeRequestDto.getSocksId();
        var outcome = outcomeRepository.findById(socksOutcomeRequestDto.getOutcomeId()).orElseThrow();
        var socks = socksRepository.findById(socksOutcomeRequestDto.getSocksId()).orElseThrow();
        var foundSocksOutcome = socksOutcomeRepository.findByOutcomeAndSocks(outcome, socks);

        if (foundSocksOutcome.isPresent()) {
            SocksOutcome socksOutcome = socksOutcomeMapper.update(socksOutcomeRequestDto, foundSocksOutcome.get());
            log.info("Socks outcome for outcome with ID: {} and socksId: {}, was updated, new quantity: {}",
                    socksOutcomeRequestDto.getOutcomeId(), socksId, socksOutcomeRequestDto.getQuantity());
            socksOutcomeRepository.saveAndFlush(socksOutcome);
        } else {
            log.info("Add new socks with ID: {} to outcome with ID: {}", socksId, socksOutcomeRequestDto.getOutcomeId());
            SocksOutcome socksOutcome = socksOutcomeMapper.dtoToSocksOutcome(socksOutcomeRequestDto);
            socksOutcomeRepository.save(socksOutcome);
        }
    }

    public boolean acceptOutcome(Long outcomeId) {
        var foundOutcome = outcomeRepository.findById(outcomeId);
        if (foundOutcome.isPresent()) {
            var socksList = foundOutcome.get().getItems();
            for (SocksOutcome socksOutcome : socksList) {
                var warehouseCreateDto = new WarehouseCreateDto(socksOutcome.getQuantity(), socksOutcome.getSocks().getId());
                warehouseService.deleteSocksFromWarehouse(warehouseCreateDto);
            }
            log.info("Outcome with ID: {} was accepted in warehouse", outcomeId);
            return true;
        } else {
            log.info("Outcome with ID: {} not found", outcomeId);
            return false;
        }
    }
}
