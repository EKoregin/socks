package org.koregin.socks_app.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.entity.*;
import org.koregin.socks_app.database.repository.*;
import org.koregin.socks_app.dto.*;
import org.koregin.socks_app.exceptions.NotCorrectOperationException;
import org.koregin.socks_app.mapper.SocksIncomeMapper;
import org.koregin.socks_app.mapper.SocksOutcomeMapper;
import org.koregin.socks_app.service.IncomeService;
import org.koregin.socks_app.service.OutcomeService;
import org.koregin.socks_app.service.SocksService;
import org.koregin.socks_app.service.WarehouseService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class SocksFacade {

    private final IncomeService incomeService;
    private final IncomeRepository incomeRepository;
    private final SocksIncomeRepository socksIncomeRepository;
    private final SocksRepository socksRepository;
    private final SocksService socksService;
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
                    Integer total = warehouseRepository.findBySocks(socks)
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

    public Long createIncome(IncomeRequestDto incomeRequestDto) {
        return incomeService.create(incomeRequestDto);
    }

    public void addSocksToIncome(SocksIncomeRequestDto socksIncomeRequestDto) {
        Long socksId = socksIncomeRequestDto.getSocksId();
        Long incomeId = socksIncomeRequestDto.getIncomeId();
        Integer quantity = socksIncomeRequestDto.getQuantity();

        Income income = incomeRepository.findById(incomeId).orElseThrow(() ->
                new NoSuchElementException("Income with ID: " + incomeId + " not found"));

        Socks socks = socksRepository.findById(socksId).orElseThrow(() ->
                new NoSuchElementException("Socks with ID: " + socksId + " not found"));

        Optional<SocksIncome> foundSocksIncome = socksIncomeRepository.findByIncomeAndSocks(income, socks);

        if (foundSocksIncome.isPresent()) {
            foundSocksIncome.get().setQuantity(quantity);
            log.info("Socks income for income with ID: {} and socksId: {}, was updated, new quantity: {}",
                    incomeId, socksId, quantity);
            socksIncomeRepository.saveAndFlush(foundSocksIncome.get());
        } else {
            log.info("Add new socks with ID: {} to income with ID: {}", socksId, socksIncomeRequestDto.getIncomeId());
            socksIncomeRepository.save(socksIncomeMapper.dtoToSocksIncome(socksIncomeRequestDto, socks, income));
        }
    }

    public boolean acceptIncome(Long incomeId) {
        Optional<Income> foundIncome = incomeRepository.findById(incomeId);
        if (foundIncome.isPresent()) {
            List<SocksIncome> socksList = foundIncome.get().getItems();
            for (SocksIncome socksIncome : socksList) {
                WarehouseRequestDto warehouseRequestDto = new WarehouseRequestDto(socksIncome.getQuantity(), socksIncome.getSocks().getId());
                warehouseService.addSocksToWarehouse(warehouseRequestDto);
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
        Long socksId = socksOutcomeRequestDto.getSocksId();
        Long outcomeId = socksOutcomeRequestDto.getOutcomeId();
        Integer quantity = socksOutcomeRequestDto.getQuantity();

        Outcome outcome = outcomeRepository.findById(outcomeId).orElseThrow(() ->
                new NoSuchElementException("Outcome with ID: " + outcomeId + " not found"));

        Socks socks = socksRepository.findById(socksId).orElseThrow(() ->
                new NoSuchElementException("Socks with ID: " + socksId + " not found"));

        Optional<SocksOutcome> foundSocksOutcome = socksOutcomeRepository.findByOutcomeAndSocks(outcome, socks);

        if (foundSocksOutcome.isPresent()) {
            foundSocksOutcome.get().setQuantity(quantity);
            log.info("SocksOutcome for outcome with ID: {} and socksId: {}, was updated, new quantity: {}",
                    outcomeId, socksId, quantity);
            socksOutcomeRepository.saveAndFlush(foundSocksOutcome.get());
        } else {
            log.info("Add new socks with ID: {} to outcome with ID: {}", socksId, socksOutcomeRequestDto.getOutcomeId());
            socksOutcomeRepository.save(socksOutcomeMapper.dtoToSocksOutcome(socksOutcomeRequestDto, socks, outcome));
        }
    }

    public boolean acceptOutcome(Long outcomeId) {
        Optional<Outcome> foundOutcome = outcomeRepository.findById(outcomeId);
        if (foundOutcome.isPresent()) {
            List<SocksOutcome> socksList = foundOutcome.get().getItems();
            for (SocksOutcome socksOutcome : socksList) {
                WarehouseRequestDto warehouseRequestDto = new WarehouseRequestDto(socksOutcome.getQuantity(), socksOutcome.getSocks().getId());
                warehouseService.deleteSocksFromWarehouse(warehouseRequestDto);
            }
            log.info("Outcome with ID: {} was accepted in warehouse", outcomeId);
            return true;
        } else {
            log.info("Outcome with ID: {} not found", outcomeId);
            return false;
        }
    }

    public SocksResponseDto create(SocksRequestDto socksRequestDto) {
        return socksService.create(socksRequestDto);
    }

    public SocksResponseDto update(Long id, SocksRequestDto socksRequestDto) {
        return socksService.update(id, socksRequestDto);
    }
}
