package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentLog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentLogResponseTest extends TestBase {

	private TournamentLog entity;

	@Override
	public void init() {
		entity = valid(TournamentLog.class);
	}

	@Test
	void should_instantiate_from_entity() {
		assertThat(TournamentLogResponse.from(entity))
			.isEqualTo(new TournamentLogResponse(entity.getId(), entity.getPlayerId(), entity.getType(),
					entity.getAmount(), entity.getMessage(), entity.getTimestamp()));
	}

}
