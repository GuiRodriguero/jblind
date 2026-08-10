package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

import com.gui.jblind.tournament.web.TournamentDetailResponse.TournamentLevelResponse;
import com.gui.jblind.tournament.web.TournamentDetailResponse.TournamentPlayerResponse;
import com.gui.jblind.tournament.web.TournamentDetailResponse.TournamentPrizeResponse;

class TournamentDetailResponseTest {

	private final Tournament entity = valid(Tournament.class);

	@Test
	void should_convert_to_entity() {
		List<TournamentLogResponse> logs = valid(TournamentLogResponse.class, 3);

		TournamentDetailResponse expected = new TournamentDetailResponse(entity.getId(), entity.getName(),
				entity.getScheduledAt(), entity.getExpectedPlayers(), entity.getBuyIn(), entity.getStartingStack(),
				entity.getStatus().name(), entity.isAllowRebuys(), entity.isAllowAddOn(),
				entity.getLevels().stream().map(TournamentLevelResponse::of).toList(),
				entity.getPlayers().stream().map(TournamentPlayerResponse::of).toList(),
				TournamentPrizeResponse.of(entity.getPrize()), logs);

		assertThat(TournamentDetailResponse.of(entity, logs)).isEqualTo(expected);
	}

}
