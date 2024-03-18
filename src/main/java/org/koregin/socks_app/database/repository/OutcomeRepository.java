package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
}
