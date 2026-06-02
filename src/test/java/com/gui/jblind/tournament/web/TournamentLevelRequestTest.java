package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentLevel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentLevelRequestTest extends TestBase {

	private final TournamentLevelRequest request = valid(TournamentLevelRequest.class);

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentLevelRequest.to(request)).isEqualTo(expected(request));
	}

	private TournamentLevel expected(TournamentLevelRequest request) {
		return TournamentLevel.builder()
			.roundNumber(request.roundNumber())
			.smallBlind(request.smallBlind())
			.bigBlind(request.bigBlind())
			.ante(request.ante())
			.durationInMinutes(request.durationInMinutes())
			.isBreak(request.isBreak())
			.shouldColorUp(request.shouldColorUp())
			.build();
	}

}
