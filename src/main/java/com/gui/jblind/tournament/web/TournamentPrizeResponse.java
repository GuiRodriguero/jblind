package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.PrizeMode;
import com.gui.jblind.tournament.TournamentPrize;

import java.util.List;

record TournamentPrizeResponse(PrizeMode mode, List<TournamentPrizePayoutResponse> payouts) {

	public static TournamentPrizeResponse of(TournamentPrize entity) {
		if (entity == null) {
			return null;
		}

		return new TournamentPrizeResponse(entity.getMode(),
				entity.getPayouts().stream().map(TournamentPrizePayoutResponse::of).toList());
	}

}
