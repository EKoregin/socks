package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.database.repository.WarehouseRepository;
import org.koregin.socks_app.dto.WarehouseRequestDto;
import org.koregin.socks_app.exceptions.NotEnoughSocksInWarehouseException;
import org.koregin.socks_app.mapper.WarehouseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepository warehouseRepository;
    private final SocksRepository socksRepository;

    @Override
    public Integer addSocksToWarehouse(WarehouseRequestDto warehouseRequestDto) {
        Integer totalSocksInWarehouse = warehouseRequestDto.getTotal();
        log.info("Add {} socks with ID: {} to warehouse",  totalSocksInWarehouse, warehouseRequestDto.getSocksId());
        Long socksId = warehouseRequestDto.getSocksId();
        Optional<Warehouse> foundWarehouse = warehouseRepository.findBySocksId(socksId);
        Socks socks = socksRepository.findById(socksId).orElseThrow(() ->
                new NoSuchElementException("Socks with ID: " + socksId + " not found"));

        if (foundWarehouse.isPresent()) {
            log.info("Found warehouse id: {}", foundWarehouse.get().getId());
            Integer currentTotalWarehouse = foundWarehouse.get().getTotal();
            totalSocksInWarehouse += currentTotalWarehouse;
            log.info("Warehouse for socks already exists. Update total count. Old total: {}, New total: {}", currentTotalWarehouse, totalSocksInWarehouse);
            foundWarehouse.get().setTotal(totalSocksInWarehouse);
            warehouseRepository.saveAndFlush(foundWarehouse.get());
        } else {
            Warehouse newWarehouse = warehouseMapper.dtoToWarehouse(warehouseRequestDto, socks);
            log.info("Warehouse for socks with ID: {} not exists. Create new ones", newWarehouse.getSocks().getId());
            log.info("SocksId: {}, Total: {}", newWarehouse.getSocks().getId(), newWarehouse.getTotal());
            warehouseRepository.save(newWarehouse);
        }
        return totalSocksInWarehouse;
    }

    @Override
    public Integer deleteSocksFromWarehouse(WarehouseRequestDto warehouseRequestDto) {
        Integer totalSocksInWarehouse = warehouseRequestDto.getTotal();
        log.info("Remove {} of socks with ID: {} from warehouse", totalSocksInWarehouse, warehouseRequestDto.getSocksId());
        Long socksId = warehouseRequestDto.getSocksId();
        Warehouse foundWarehouse = warehouseRepository.findBySocksId(socksId).orElseThrow(() ->
                new NoSuchElementException("Warehouse with socksId: " + socksId + " not found"));

        socksRepository.findById(socksId).orElseThrow(() ->
                new NoSuchElementException("Socks with ID: " + socksId + " not found"));

        Integer currentTotalWarehouse = foundWarehouse.getTotal();
        totalSocksInWarehouse = currentTotalWarehouse - totalSocksInWarehouse;
        log.info("Warehouse for socks already exists. Update total count. Old total: {}, New total: {}", currentTotalWarehouse, totalSocksInWarehouse);
        if (totalSocksInWarehouse < 0) {
            log.info("Not enough {} socks in warehouse: ",  Math.abs(totalSocksInWarehouse));
            throw new NotEnoughSocksInWarehouseException("Not enough " + Math.abs(totalSocksInWarehouse) + " socks in Warehouse");
        }

        foundWarehouse.setTotal(totalSocksInWarehouse);
        warehouseRepository.saveAndFlush(foundWarehouse);

        return totalSocksInWarehouse;
    }
}
