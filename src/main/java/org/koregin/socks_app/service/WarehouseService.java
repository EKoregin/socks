package org.koregin.socks_app.service;

import org.koregin.socks_app.dto.WarehouseRequestDto;

public interface WarehouseService {

    Integer addSocksToWarehouse(WarehouseRequestDto warehouseRequestDto);
    Integer deleteSocksFromWarehouse(WarehouseRequestDto warehouseRequestDto);
}
