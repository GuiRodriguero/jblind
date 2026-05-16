package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TournamentDetailResponse(Long id, String name, LocalDateTime scheduledAt, Integer expectedPlayers,
		BigDecimal buyIn, Integer startingStack, String status, List<TournamentLevelResponse> levels) {

	public static TournamentDetailResponse of(Tournament entity) {
		return new TournamentDetailResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getExpectedPlayers(), entity.getBuyIn(), entity.getStartingStack(), entity.getStatus().name(),
				entity.getLevels().stream().map(TournamentLevelResponse::of).toList());
	}

}