package com.gui.jblind.cashgame;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class CashGameLogQuery {

	private final CashGameLogRepository repository;

	List<CashGameLog> findAllByCashGameId(String cashGameId) {
		return repository.findAllByCashGameIdOrderByTimestampDesc(cashGameId);
	}

}
