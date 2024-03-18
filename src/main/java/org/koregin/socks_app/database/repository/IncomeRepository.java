package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
