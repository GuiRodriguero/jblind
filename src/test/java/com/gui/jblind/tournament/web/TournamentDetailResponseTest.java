package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentDetailResponseTest {

	private final Tournament entity = valid(Tournament.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentDetailResponse.of(entity)).isEqualTo(expected(entity));
	}

	private TournamentDetailResponse expected(Tournament entity) {
		return new TournamentDetailResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getExpectedPlayers(), entity.getBuyIn(), entity.getStartingStack(), entity.getStatus().name(),
				entity.getLevels().stream().map(TournamentLevelResponse::of).toList(),
				entity.getPlayers().stream().map(TournamentPlayerResponse::of).toList(),
				TournamentPrizeResponse.of(entity.getPrize()));
	}

}
