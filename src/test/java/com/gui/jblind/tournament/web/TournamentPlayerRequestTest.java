package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentPlayer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentPlayerRequestTest extends TestBase {

	private final TournamentPlayerRequest request = valid(TournamentPlayerRequest.class);

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_convert_to_entity() {
		assertThat(request.to()).isEqualTo(expected(request));
	}

	private TournamentPlayer expected(TournamentPlayerRequest request) {
		return TournamentPlayer.builder().name(request.name()).build();
	}

}
