package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocksOutcomeRepository extends JpaRepository<SocksOutcome, Long> {

    Optional<SocksOutcome> findByOutcomeAndSocks(Outcome outcome, Socks socks);
}
