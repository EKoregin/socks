package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.repository.WarehouseRepository;
import org.koregin.socks_app.dto.WarehouseCreateDto;
import org.koregin.socks_app.exceptions.NotEnoughSocksInWarehouseException;
import org.koregin.socks_app.mapper.WarehouseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepository warehouseRepository;

    @Override
    public Integer addSocksToWarehouse(WarehouseCreateDto warehouseCreateDto) {
        return updateWarehouse(warehouseCreateDto, true);
    }

    @Override
    public Integer deleteSocksFromWarehouse(WarehouseCreateDto warehouseCreateDto) {
        return updateWarehouse(warehouseCreateDto, false);
    }

    private Integer updateWarehouse(WarehouseCreateDto warehouseCreateDto, boolean addToWarehouse) {
        log.info("WarehouseCreateDto: total: {}", warehouseCreateDto.getTotal());
        var newWarehouse = warehouseMapper.dtoToWarehouse(warehouseCreateDto);
        Integer totalSocksInWarehouse = newWarehouse.getTotal();
        var foundWarehouse = warehouseRepository.findBySocks(newWarehouse.getSocks());
        if (foundWarehouse.isPresent()) {
            log.info("Warehouse for socks already exists. Update total count");
            if (addToWarehouse) {
                totalSocksInWarehouse += foundWarehouse.get().getTotal();
            } else {
                totalSocksInWarehouse = foundWarehouse.get().getTotal() - totalSocksInWarehouse;
                if (totalSocksInWarehouse < 0) {
                    throw new NotEnoughSocksInWarehouseException("Not enough socks in Warehouse: " + Math.abs(totalSocksInWarehouse));
                }
            }
            newWarehouse = warehouseMapper.update(warehouseCreateDto, foundWarehouse.get());
            newWarehouse.setTotal(totalSocksInWarehouse);
            warehouseRepository.saveAndFlush(newWarehouse);
        } else {
            log.info("Warehouse for socks with ID: {} not exists. Create new ones", newWarehouse.getSocks().getId());
            log.info("SocksId: {}, Total: {}", newWarehouse.getSocks().getId(), newWarehouse.getTotal());
            warehouseRepository.save(newWarehouse);
        }
        return totalSocksInWarehouse;
    }
}
