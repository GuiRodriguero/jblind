package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPlayer;

public record TournamentPlayerRequest(String name) {

	public TournamentPlayer to() {
		return TournamentPlayer.builder().name(name).build();
	}
}
