package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.cashgame.CashGameStatus.FINISHED;
import static com.gui.jblind.cashgame.CashGameStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

class CashGameTest extends TestBase {

	private CashGame entity;

	@Override
	public void init() {
		entity = valid(CashGame.class);
	}

	@Test
	void should_add_player() {
		CashGamePlayer player = valid(CashGamePlayer.class);

		entity.addPlayer(player);

		assertThat(entity.getPlayers()).contains(player);
	}

	@Test
	void should_start_cash_game() {
		assertThat(entity.start().getStatus()).isEqualTo(IN_PROGRESS);
	}

	@Test
	void should_finish_cash_game() {
		assertThat(entity.finish().getStatus()).isEqualTo(FINISHED);
	}

}
