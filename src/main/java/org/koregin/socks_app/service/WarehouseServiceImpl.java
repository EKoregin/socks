package org.koregin.socks_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.database.repository.WarehouseRepository;
import org.koregin.socks_app.dto.WarehouseCreateDto;
import org.koregin.socks_app.mapper.WarehouseCreateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseCreateMapper warehouseCreateMapper;
    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public Integer updateWarehouse(WarehouseCreateDto warehouseCreateDto) {
        var newWarehouse = warehouseCreateMapper.map(warehouseCreateDto);
        Integer totalSocksInWarehouse = newWarehouse.getTotal();
        var foundWarehouse = warehouseRepository.findBySocks(newWarehouse.getSocks());
        if (foundWarehouse.isPresent()) {
            log.info("Warehouse for socks already exists. Update total count");
            newWarehouse = warehouseCreateMapper.map(warehouseCreateDto, foundWarehouse.get());
            totalSocksInWarehouse += foundWarehouse.get().getTotal();
            newWarehouse.setTotal(totalSocksInWarehouse);
            warehouseRepository.saveAndFlush(newWarehouse);

        } else {
            log.info("Warehouse for socks not exists. Create new ones");
            warehouseRepository.save(newWarehouse);
        }
        return totalSocksInWarehouse;
    }
}
