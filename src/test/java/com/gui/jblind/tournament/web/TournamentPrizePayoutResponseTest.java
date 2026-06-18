package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrizePayout;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentPrizePayoutResponseTest {

	private final TournamentPrizePayout entity = valid(TournamentPrizePayout.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentPrizePayoutResponse.of(entity)).isEqualTo(expected(entity));
	}

	private TournamentPrizePayoutResponse expected(TournamentPrizePayout entity) {
		return new TournamentPrizePayoutResponse(entity.getPosition(), entity.getValue(), entity.getPercentage());
	}

}
