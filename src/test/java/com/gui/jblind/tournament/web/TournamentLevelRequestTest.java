package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLevel;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentLevelRequestTest {

	private final TournamentLevelRequest request = valid(TournamentLevelRequest.class);

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
