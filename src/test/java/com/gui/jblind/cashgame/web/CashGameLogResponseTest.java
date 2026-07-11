package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGameLog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CashGameLogResponseTest extends TestBase {

	private CashGameLog entity;

	@Override
	public void init() {
		entity = valid(CashGameLog.class);
	}

	@Test
	void should_instantiate_from_entity() {
		assertThat(CashGameLogResponse.from(entity))
			.isEqualTo(new CashGameLogResponse(entity.getId(), entity.getPlayerId(), entity.getType(),
					entity.getAmount(), entity.getMessage(), entity.getTimestamp()));
	}

}
