package com.gui.jblind.cashgame;

import com.gui.jblind.cashgame.web.CashGameLogRequest;
import com.gui.jblind.cashgame.web.CashGameLogResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CashGameLogService {

	private final CashGameLogRepository repository;

	private final CashGamePlayerService playerService;

	public CashGameLogResponse createLog(String cashGameId, CashGameLogRequest request) {
		CashGameLog log = CashGameLog.builder()
			.cashGameId(cashGameId)
			.playerId(request.cashGamePlayerId())
			.type(request.type())
			.amount(request.amount())
			.message(request.message())
			.timestamp(LocalDateTime.now())
			.build();

		playerService.updatePlayerStats(request);

		return CashGameLogResponse.from(repository.save(log));
	}

}
