package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLevel;

record TournamentLevelResponse(Integer roundNumber, Integer smallBlind, Integer bigBlind, Integer ante,
		Integer durationInMinutes, boolean isBreak, boolean shouldColorUp) {

	public static TournamentLevelResponse of(TournamentLevel entity) {
		return new TournamentLevelResponse(entity.getRoundNumber(), entity.getSmallBlind(), entity.getBigBlind(),
				entity.getAnte(), entity.getDurationInMinutes(), entity.isBreak(), entity.isShouldColorUp());
	}

}