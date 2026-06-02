package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TournamentSummaryResponse(String id, String name, LocalDateTime scheduledAt, Integer expectedPlayers,
		BigDecimal buyIn, String status) {

	public static TournamentSummaryResponse of(Tournament entity) {
		return new TournamentSummaryResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getExpectedPlayers(), entity.getBuyIn(), entity.getStatus().name());
	}

}