package com.gui.jblind.cashgame;

import com.gui.jblind.cashgame.web.CashGameLogResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class CashGameLogQuery {

	private final CashGameLogRepository repository;

	List<CashGameLogResponse> findAllByCashGameId(String cashGameId) {
		return repository.findAllByCashGameIdOrderByTimestampDesc(cashGameId)
			.stream()
			.map(CashGameLogResponse::from)
			.toList();
	}

}
