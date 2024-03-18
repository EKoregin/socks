package org.koregin.socks_app.mapper;

import lombok.RequiredArgsConstructor;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.Warehouse;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.WarehouseCreateDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WarehouseCreateMapper implements GenericMapper<WarehouseCreateDto, Warehouse> {

    private final SocksRepository socksRepository;

    @Override
    public Warehouse map(WarehouseCreateDto object) {
        Warehouse warehouse = new Warehouse();

        warehouse.setTotal(object.getQuantity());
        warehouse.setSocks(getSocks(object.getSocksId()));

        return warehouse;
    }

    private Socks getSocks(Long socksId) {
        return Optional.ofNullable(socksId)
                .flatMap(socksRepository::findById)
                .orElse(null);
    }
}
