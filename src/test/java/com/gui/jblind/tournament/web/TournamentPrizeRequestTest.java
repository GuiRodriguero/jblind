package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentPrize;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentPrizeRequestTest extends TestBase {

	private final TournamentPrizeRequest request = valid(TournamentPrizeRequest.class);

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentPrizeRequest.to(request)).isEqualTo(expected(request));
	}

	@Test
	void should_return_null_when_request_is_null() {
		assertThat(TournamentPrizeRequest.to(null)).isNull();
	}

	@Test
	void should_convert_when_payouts_are_null() {
		TournamentPrizeRequest request = new TournamentPrizeRequest("FIXED", null);

		assertThat(TournamentPrizeRequest.to(request)).isEqualTo(expected(request));
	}

	private TournamentPrize expected(TournamentPrizeRequest request) {
		TournamentPrize prize = TournamentPrize.builder().mode(request.mode()).build();
		if (request.payouts() != null) {
			request.payouts().forEach(payout -> prize.addPayout(TournamentPrizePayoutRequest.to(payout)));
		}

		return prize;
	}

}
