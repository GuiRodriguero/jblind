package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPlayer;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentPlayerResponseTest {

	private final TournamentPlayer entity = valid(TournamentPlayer.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentPlayerResponse.of(entity)).isEqualTo(expected(entity));
	}

	private TournamentPlayerResponse expected(TournamentPlayer entity) {
		return new TournamentPlayerResponse(entity.getId(), entity.getName());
	}

}
