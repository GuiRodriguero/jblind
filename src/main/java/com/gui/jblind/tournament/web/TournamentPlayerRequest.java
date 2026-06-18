package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPlayer;

public record TournamentPlayerRequest(String name) {

	public static TournamentPlayer to(TournamentPlayerRequest request) {
		return TournamentPlayer.builder().name(request.name()).build();
	}
}
