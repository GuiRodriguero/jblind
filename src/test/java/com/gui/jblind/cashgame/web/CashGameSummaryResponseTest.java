package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGame;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CashGameSummaryResponseTest extends TestBase {

	private CashGame entity;

	@Override
	public void init() {
		entity = valid(CashGame.class);
	}

	@Test
	void should_instantiate_from_entity() {
		assertThat(CashGameSummaryResponse.of(entity)).isEqualTo(new CashGameSummaryResponse(entity.getId(),
				entity.getName(), entity.getScheduledAt(), entity.getSmallBlind(), entity.getBigBlind(),
				entity.getMinBuyIn(), entity.getMaxBuyIn(), entity.getPlayers().size(), entity.getStatus().name()));
	}

}
