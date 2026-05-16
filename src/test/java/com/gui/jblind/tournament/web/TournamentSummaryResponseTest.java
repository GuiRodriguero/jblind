package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentSummaryResponseTest {

	private final Tournament entity = valid(Tournament.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentSummaryResponse.of(entity)).isEqualTo(expected(entity));
	}

	private TournamentSummaryResponse expected(Tournament entity) {
		return new TournamentSummaryResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getExpectedPlayers(), entity.getBuyIn(), entity.getStatus().name());
	}

}
