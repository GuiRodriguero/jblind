package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGamePlayer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CashGamePlayerResponseTest extends TestBase {

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_instantiate_from_entity() {
		CashGamePlayer entity = valid(CashGamePlayer.class);

		assertThat(CashGamePlayerResponse.of(entity)).isEqualTo(new CashGamePlayerResponse(entity.getId(),
				entity.getName(), entity.getTotalInvested(), entity.getCurrentStack()));
	}

}
