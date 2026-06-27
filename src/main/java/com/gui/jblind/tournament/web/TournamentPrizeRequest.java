package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrize;

import java.util.List;

public record TournamentPrizeRequest(String mode, List<TournamentPrizePayoutRequest> payouts) {

	public TournamentPrize to() {
		TournamentPrize prize = TournamentPrize.builder().mode(mode).build();

		if (payouts != null) {
			payouts.forEach(payout -> prize.addPayout(payout.to()));
		}

		return prize;
	}
}
