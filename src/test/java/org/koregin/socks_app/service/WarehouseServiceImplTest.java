package org.koregin.socks_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.database.repository.WarehouseRepository;
import org.koregin.socks_app.dto.WarehouseRequestDto;
import org.koregin.socks_app.exceptions.NotEnoughSocksInWarehouseException;
import org.koregin.socks_app.mapper.WarehouseMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    private static final Long SOCKS_ID = 99L;
    private static final Integer TOTAL_SOCKS = 10;
    private static final Integer DEL_SOCKS = 5;
    private static final Integer SMALL_TOTAL_SOCKS = 1;
    private static final Long WAREHOUSE_ID = 25L;

    @Mock
    private WarehouseMapper warehouseMapper;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SocksRepository socksRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Test
    void addSocksToNewWarehouse() {

        Socks socks = new Socks(SOCKS_ID, "Socks", "BLACK", 90);
        WarehouseRequestDto requestDto = new WarehouseRequestDto(TOTAL_SOCKS, SOCKS_ID);
        Warehouse newWarehouse = new Warehouse(null, TOTAL_SOCKS, socks);
        Warehouse savedWarehouse = new Warehouse(WAREHOUSE_ID, TOTAL_SOCKS, socks);
        Optional<Warehouse> foundWarehouse = Optional.empty();


        when(socksRepository.findById(SOCKS_ID)).thenReturn(Optional.of(socks));
        when(warehouseMapper.dtoToWarehouse(requestDto, socks)).thenReturn(newWarehouse);
        when(warehouseRepository.findBySocksId(SOCKS_ID)).thenReturn(foundWarehouse);
        when(warehouseRepository.save(newWarehouse)).thenReturn(savedWarehouse);

        var actualResult = warehouseService.addSocksToWarehouse(requestDto);

        assertEquals(TOTAL_SOCKS, actualResult);

    }

    @Test
    void addSocksToExistedWarehouse() {
        Socks socks = new Socks(SOCKS_ID, "Socks", "BLACK", 90);
        WarehouseRequestDto requestDto = new WarehouseRequestDto(TOTAL_SOCKS, SOCKS_ID);
        Integer newTotalSocks = TOTAL_SOCKS + TOTAL_SOCKS;
        Warehouse savedWarehouse = new Warehouse(WAREHOUSE_ID, newTotalSocks, socks);
        Warehouse foundWarehouse = new Warehouse(WAREHOUSE_ID, TOTAL_SOCKS, socks);

        when(socksRepository.findById(SOCKS_ID)).thenReturn(Optional.of(socks));
        when(warehouseRepository.findBySocksId(SOCKS_ID)).thenReturn(Optional.of(foundWarehouse));
        when(warehouseRepository.saveAndFlush(foundWarehouse)).thenReturn(savedWarehouse);

        var actualResult = warehouseService.addSocksToWarehouse(requestDto);

        assertEquals(newTotalSocks, actualResult);
    }

    @Test
    void deleteSocksFromWarehouse() {
        Socks socks = new Socks(SOCKS_ID, "Socks", "BLACK", 90);
        WarehouseRequestDto requestDto = new WarehouseRequestDto(DEL_SOCKS, SOCKS_ID);
        Integer newTotalSocks = TOTAL_SOCKS - DEL_SOCKS;
        Warehouse savedWarehouse = new Warehouse(WAREHOUSE_ID, newTotalSocks, socks);
        Warehouse foundWarehouse = new Warehouse(WAREHOUSE_ID, TOTAL_SOCKS, socks);

        when(socksRepository.findById(SOCKS_ID)).thenReturn(Optional.of(socks));
        when(warehouseRepository.findBySocksId(SOCKS_ID)).thenReturn(Optional.of(foundWarehouse));
        when(warehouseRepository.saveAndFlush(foundWarehouse)).thenReturn(savedWarehouse);

        var actualResult = warehouseService.deleteSocksFromWarehouse(requestDto);

        assertEquals(newTotalSocks, actualResult);
    }

    @Test
    void deleteSocksFromWarehouseWhenSocksNotEnough() {
        Socks socks = new Socks(SOCKS_ID, "Socks", "BLACK", 90);
        WarehouseRequestDto requestDto = new WarehouseRequestDto(DEL_SOCKS, SOCKS_ID);
        int newTotalSocks = DEL_SOCKS - SMALL_TOTAL_SOCKS;
        Warehouse foundWarehouse = new Warehouse(WAREHOUSE_ID, SMALL_TOTAL_SOCKS, socks);

        when(socksRepository.findById(SOCKS_ID)).thenReturn(Optional.of(socks));
        when(warehouseRepository.findBySocksId(SOCKS_ID)).thenReturn(Optional.of(foundWarehouse));

        var exception = assertThrows(NotEnoughSocksInWarehouseException.class, () -> warehouseService.deleteSocksFromWarehouse(requestDto));

        assertThat(exception.getMessage()).isEqualTo("Not enough " + newTotalSocks + " socks in Warehouse");
    }

}