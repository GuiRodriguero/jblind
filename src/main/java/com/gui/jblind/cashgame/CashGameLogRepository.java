package com.gui.jblind.cashgame;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashGameLogRepository extends JpaRepository<CashGameLog, Long> {

	List<CashGameLog> findAllByCashGameIdOrderByTimestampDesc(String cashGameId);

}
