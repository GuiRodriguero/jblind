package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrizePayout;

import java.math.BigDecimal;

record TournamentPrizePayoutResponse(Integer position, BigDecimal value, BigDecimal percentage) {

	public static TournamentPrizePayoutResponse of(TournamentPrizePayout entity) {
		return new TournamentPrizePayoutResponse(entity.getPosition(), entity.getValue(), entity.getPercentage());
	}

}
