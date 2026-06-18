package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrizePayout;

import java.math.BigDecimal;

public record TournamentPrizePayoutRequest(Integer position, BigDecimal value, BigDecimal percentage) {

	public static TournamentPrizePayout to(TournamentPrizePayoutRequest request) {
		return TournamentPrizePayout.builder()
			.position(request.position())
			.value(request.value())
			.percentage(request.percentage())
			.build();
	}
}
