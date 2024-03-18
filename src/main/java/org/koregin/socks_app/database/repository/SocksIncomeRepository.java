package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.Income;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.entity.SocksIncome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocksIncomeRepository extends JpaRepository<SocksIncome, Long> {

    Optional<SocksIncome> findByIncomeAndSocks(Income income, Socks socks);
}
