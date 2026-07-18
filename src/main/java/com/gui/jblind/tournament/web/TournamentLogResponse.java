package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLog;
import com.gui.jblind.tournament.TournamentLogType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TournamentLogResponse(String id, String playerId, TournamentLogType type, BigDecimal amount,
		String message, LocalDateTime timestamp) {

	public static TournamentLogResponse from(TournamentLog log) {
		return new TournamentLogResponse(log.getId(), log.getPlayerId(), log.getType(), log.getAmount(),
				log.getMessage(), log.getTimestamp());
	}

}
