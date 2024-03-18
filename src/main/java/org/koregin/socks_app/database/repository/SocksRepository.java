package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.Socks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocksRepository extends JpaRepository<Socks, Long> {

    List<Socks> findAllByColorAndCottonPartAfter(String color, Integer cottonPart);

    List<Socks> findAllByColorAndCottonPartBefore(String color, Integer cottonPart);

    List<Socks> findAllByColorAndCottonPart(String color, Integer cottonPart);

}
