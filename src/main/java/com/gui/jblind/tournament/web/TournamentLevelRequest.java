package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLevel;

public record TournamentLevelRequest(Integer roundNumber, Integer smallBlind, Integer bigBlind, Integer ante,
		Integer durationInMinutes, boolean isBreak, boolean shouldColorUp) {

	public TournamentLevel to() {
		return TournamentLevel.builder()
			.roundNumber(roundNumber)
			.smallBlind(smallBlind)
			.bigBlind(bigBlind)
			.ante(ante)
			.durationInMinutes(durationInMinutes)
			.isBreak(isBreak)
			.shouldColorUp(shouldColorUp)
			.build();
	}
}