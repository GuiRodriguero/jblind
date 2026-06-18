package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentPrizeTest extends TestBase {

	private TournamentPrize entity;

	@Override
	public void init() {
		entity = valid(TournamentPrize.class);
	}

	@Test
	void should_add_level() {
		TournamentPrizePayout payout = valid(TournamentPrizePayout.class);

		entity.addPayout(payout);

		assertThat(entity.getPayouts()).contains(payout);
	}

}
