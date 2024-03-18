package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.SocksIncome;
import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.database.repository.IncomeRepository;
import org.koregin.socks_app.database.repository.SocksIncomeRepository;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.database.repository.WarehouseRepository;
import org.koregin.socks_app.dto.*;
import org.koregin.socks_app.mapper.IncomeCreateMapper;
import org.koregin.socks_app.mapper.SocksIncomeCreateMapper;
import org.koregin.socks_app.mapper.SocksMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final IncomeRepository incomeRepository;
    private final SocksMapper socksMapper;
    private final IncomeCreateMapper incomeCreateMapper;
    private final SocksIncomeCreateMapper socksIncomeCreateMapper;
    private final SocksIncomeRepository socksIncomeRepository;
    private final WarehouseService warehouseService;
    private final WarehouseRepository warehouseRepository;

    @Override
    public Integer countByParams(String color, Integer cottonPart, String operation) throws BadRequestException {
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

    private void checkOperation(String operation) throws BadRequestException {
        if (!"moreThan".equals(operation) && !"lessThan".equals(operation) && !"equal".equals(operation)) {
            throw new BadRequestException("Not correct operation parameter");
        }
    }


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

    @Override
    public Long incomeCreate(IncomeCreateDto incomeCreateDto) {
        log.info("Create income with employeeId: {}", incomeCreateDto.getEmployeeId());
        return Optional.of(incomeCreateDto)
                .map(incomeCreateMapper::map)
                .map(incomeRepository::save)
                .map(Income::getId)
                .orElseThrow();
    }

    @Override
    public void addSocksToIncome(SocksIncomeCreateDto socksIncomeCreateDto) {
        var socksId = socksIncomeCreateDto.getSocksId();
        var income = incomeRepository.findById(socksIncomeCreateDto.getIncomeId()).orElseThrow();
        var socks = socksRepository.findById(socksIncomeCreateDto.getSocksId()).orElseThrow();
        var foundSocksIncome = socksIncomeRepository.findByIncomeAndSocks(income, socks);

        if (foundSocksIncome.isPresent()) {
            log.info("Add existed socks with ID: {} to income with ID: {}", socksId, socksIncomeCreateDto.getIncomeId());
            SocksIncome socksIncome = foundSocksIncome.get();
            socksIncome.setQuantity(socksIncomeCreateDto.getQuantity());
            socksIncomeRepository.saveAndFlush(socksIncome);
        } else {
            log.info("Add new socks with ID: {} to income with ID: {}", socksId, socksIncomeCreateDto.getIncomeId());
            SocksIncome socksIncome = socksIncomeCreateMapper.map(socksIncomeCreateDto);
            socksIncomeRepository.save(socksIncome);
        }
    }

    @Override
    public boolean acceptIncome(Long incomeId) {
        var foundIncome = incomeRepository.findById(incomeId);
        if (foundIncome.isPresent()) {
            var socksList = foundIncome.get().getItems();
            for (SocksIncome socksIncome : socksList) {
                var warehouseCreateDto = new WarehouseCreateDto(socksIncome.getQuantity(), socksIncome.getSocks().getId());
                warehouseService.updateWarehouse(warehouseCreateDto);
            }
            log.info("Income with ID: {} was accepted in warehouse", incomeId);
            return true;
        } else {
            log.info("Income with ID: {} not found", incomeId);
            return false;
        }
    }
}
