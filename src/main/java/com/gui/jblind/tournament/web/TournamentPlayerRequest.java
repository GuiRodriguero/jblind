package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPlayer;

import java.math.BigDecimal;

public record TournamentPlayerRequest(String name, BigDecimal totalInvested) {

	public TournamentPlayer to() {
		return TournamentPlayer.builder().name(name).totalInvested(totalInvested).build();
	}
}
