package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLevel;

public record TournamentLevelRequest(Integer roundNumber, Integer smallBlind, Integer bigBlind, Integer ante,
		Integer durationInMinutes, boolean isBreak) {

	public static TournamentLevel to(TournamentLevelRequest request) {
		return TournamentLevel.builder()
			.roundNumber(request.roundNumber())
			.smallBlind(request.smallBlind())
			.bigBlind(request.bigBlind())
			.ante(request.ante())
			.durationInMinutes(request.durationInMinutes())
			.isBreak(request.isBreak())
			.build();
	}
}