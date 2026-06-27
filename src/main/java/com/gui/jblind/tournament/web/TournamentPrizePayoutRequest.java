package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrizePayout;

import java.math.BigDecimal;

public record TournamentPrizePayoutRequest(Integer position, BigDecimal value, BigDecimal percentage) {

	public TournamentPrizePayout to() {
		return TournamentPrizePayout.builder().position(position).value(value).percentage(percentage).build();
	}
}
