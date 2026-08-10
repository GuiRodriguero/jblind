package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentPrizePayout;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentPrizePayoutResponseTest extends TestBase {

	private final TournamentPrizePayout entity = valid(TournamentPrizePayout.class);

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_instantiate_from_entity() {
		assertThat(TournamentPrizePayoutResponse.of(entity)).isEqualTo(expected(entity));
	}

	private TournamentPrizePayoutResponse expected(TournamentPrizePayout entity) {
		return new TournamentPrizePayoutResponse(entity.getPosition(), entity.getValue(), entity.getPercentage());
	}

}
