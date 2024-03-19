package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.repository.WarehouseRepository;
import org.koregin.socks_app.dto.WarehouseCreateDto;
import org.koregin.socks_app.mapper.WarehouseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public Integer updateWarehouse(WarehouseCreateDto warehouseCreateDto) {
        log.info("WarehouseCreateDto: total: {}", warehouseCreateDto.getTotal());
        var newWarehouse = warehouseMapper.dtoToWarehouse(warehouseCreateDto);
        Integer totalSocksInWarehouse = newWarehouse.getTotal();
        log.info("Total socks: {}", totalSocksInWarehouse);
        var foundWarehouse = warehouseRepository.findBySocks(newWarehouse.getSocks());
        if (foundWarehouse.isPresent()) {
            log.info("Warehouse for socks already exists. Update total count");
            totalSocksInWarehouse += foundWarehouse.get().getTotal();
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
