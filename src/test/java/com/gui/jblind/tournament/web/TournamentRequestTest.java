package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.Tournament;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.gui.jblind.tournament.TournamentStatus.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentRequestTest extends TestBase {

	private final TournamentRequest request = valid(TournamentRequest.class);

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_convert_to_entity() {
		assertThat(request.to()).isEqualTo(expected(request));
	}

	@Test
	void should_convert_to_entity_with_id() {
		String id = UUID.randomUUID().toString();
		assertThat(request.to(id)).isEqualTo(expectedWithId(id, request));
	}

	private Tournament expected(TournamentRequest request) {
		Tournament tournament = Tournament.builder()
			.name(request.name())
			.scheduledAt(request.scheduledAt())
			.expectedPlayers(request.expectedPlayers())
			.buyIn(request.buyIn())
			.startingStack(request.startingStack())
			.allowRebuys(request.allowRebuys())
			.allowAddOn(request.allowAddOn())
			.prize(request.prize().to())
			.status(SCHEDULED)
			.build();

		request.levels().forEach(level -> tournament.addLevel(level.to()));
		request.players().forEach(player -> tournament.addPlayer(player.to()));

		return tournament;
	}

	private Tournament expectedWithId(String id, TournamentRequest request) {
		return expected(request).toBuilder().id(id).build();
	}

}
