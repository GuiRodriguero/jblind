package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrize;

import java.util.List;

public record TournamentPrizeRequest(String mode, List<TournamentPrizePayoutRequest> payouts) {

	public static TournamentPrize to(TournamentPrizeRequest request) {
		if (request == null) {
			return null;
		}

		TournamentPrize prize = TournamentPrize.builder().mode(request.mode()).build();

		if (request.payouts() != null) {
			request.payouts().forEach(payout -> prize.addPayout(TournamentPrizePayoutRequest.to(payout)));
		}

		return prize;
	}
}
