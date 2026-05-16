package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static com.gui.jblind.tournament.TournamentStatus.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentCreateRequestTest {

	private final TournamentCreateRequest request = valid(TournamentCreateRequest.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentCreateRequest.to(request)).isEqualTo(expected(request));
	}

	private Tournament expected(TournamentCreateRequest request) {
		Tournament tournament = Tournament.builder()
			.name(request.name())
			.scheduledAt(request.scheduledAt())
			.expectedPlayers(request.expectedPlayers())
			.buyIn(request.buyIn())
			.startingStack(request.startingStack())
			.allowRebuys(request.allowRebuys())
			.allowAddOn(request.allowAddOn())
			.status(SCHEDULED)
			.build();

		request.levels().forEach(level -> tournament.addLevel(TournamentLevelRequest.to(level)));

		return tournament;
	}

}
