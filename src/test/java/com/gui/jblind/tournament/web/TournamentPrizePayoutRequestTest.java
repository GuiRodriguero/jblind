package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentPrizePayout;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentPrizePayoutRequestTest extends TestBase {

	private final TournamentPrizePayoutRequest request = valid(TournamentPrizePayoutRequest.class);

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_convert_to_entity() {
		assertThat(request.to()).isEqualTo(expected(request));
	}

	private TournamentPrizePayout expected(TournamentPrizePayoutRequest request) {
		return TournamentPrizePayout.builder()
			.position(request.position())
			.value(request.value())
			.percentage(request.percentage())
			.build();
	}

}
