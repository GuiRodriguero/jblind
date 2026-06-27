package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGame;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.gui.jblind.cashgame.CashGameStatus.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;

class CashGameRequestTest extends TestBase {

	private CashGameRequest request;

	@Override
	public void init() {
		request = valid(CashGameRequest.class);
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

	// TODO: tests to check when values are negative or blank

	public CashGame expected(CashGameRequest request) {
		CashGame cashGame = CashGame.builder()
			.name(request.name())
			.scheduledAt(request.scheduledAt())
			.minBuyIn(request.minBuyIn())
			.maxBuyIn(request.maxBuyIn())
			.smallBlind(request.smallBlind())
			.bigBlind(request.bigBlind())
			.status(SCHEDULED)
			.build();

		request.players().forEach(player -> cashGame.addPlayer(player.to()));

		return cashGame;
	}

	public CashGame expectedWithId(String id, CashGameRequest request) {
		return request.to().toBuilder().id(id).build();
	}

}
