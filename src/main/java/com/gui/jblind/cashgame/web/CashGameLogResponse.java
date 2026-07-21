package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGameLog;
import com.gui.jblind.cashgame.CashGameLogType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashGameLogResponse(String id, String playerId, CashGameLogType type, BigDecimal amount, String message,
		LocalDateTime timestamp) {

	public static CashGameLogResponse from(CashGameLog log) {
		return new CashGameLogResponse(log.getId(), log.getPlayerId(), log.getType(), log.getAmount(), log.getMessage(),
				log.getTimestamp());
	}

}
