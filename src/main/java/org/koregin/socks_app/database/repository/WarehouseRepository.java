package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findBySocks(Socks socks);

    Optional<Warehouse> findBySocksId(Long id);
}
