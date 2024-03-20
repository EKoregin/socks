package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.WarehouseCreateDto;

public interface WarehouseService {

    Integer addSocksToWarehouse(WarehouseCreateDto warehouseCreateDto);
    Integer deleteSocksFromWarehouse(WarehouseCreateDto warehouseCreateDto);
}
