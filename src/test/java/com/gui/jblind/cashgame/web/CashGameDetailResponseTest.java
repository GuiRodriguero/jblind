package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGame;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CashGameDetailResponseTest extends TestBase {

	private CashGame entity;

	@Override
	public void init() {
		entity = valid(CashGame.class);
	}

	@Test
	void should_instantiate_from_entity() {
		List<CashGameLogResponse> logs = valid(CashGameLogResponse.class, 3);

		assertThat(CashGameDetailResponse.of(entity, logs)).isEqualTo(new CashGameDetailResponse(entity.getId(),
				entity.getName(), entity.getScheduledAt(), entity.getMinBuyIn(), entity.getMaxBuyIn(),
				entity.getSmallBlind(), entity.getBigBlind(), entity.getStatus().name(),
				entity.getPlayers().stream().map(CashGamePlayerResponse::of).toList(), logs));
	}

}
