package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLevel;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

import com.gui.jblind.tournament.web.TournamentDetailResponse.TournamentLevelResponse;

class TournamentLevelResponseTest {

	private final TournamentLevel entity = valid(TournamentLevel.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentLevelResponse.of(entity)).isEqualTo(expected(entity));
	}

	private TournamentLevelResponse expected(TournamentLevel entity) {
		return new TournamentLevelResponse(entity.getRoundNumber(), entity.getSmallBlind(), entity.getBigBlind(),
				entity.getAnte(), entity.getDurationInMinutes(), entity.isBreak(), entity.isShouldColorUp());
	}

}
