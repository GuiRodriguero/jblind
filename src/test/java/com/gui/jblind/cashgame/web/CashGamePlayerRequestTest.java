package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGamePlayer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CashGamePlayerRequestTest extends TestBase {

	private CashGamePlayerRequest request;

	@Override
	public void init() {
		request = valid(CashGamePlayerRequest.class);
	}

	@Test
	void should_convert_to_entity() {
		assertThat(request.to()).isEqualTo(CashGamePlayer.builder().name(request.name()).build());
	}

}
