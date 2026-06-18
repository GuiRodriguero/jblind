package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPlayer;

record TournamentPlayerResponse(String id, String name) {

	public static TournamentPlayerResponse of(TournamentPlayer entity) {
		return new TournamentPlayerResponse(entity.getId(), entity.getName());
	}

}
