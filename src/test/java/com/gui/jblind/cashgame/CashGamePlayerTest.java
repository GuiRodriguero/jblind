package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class CashGamePlayerTest extends TestBase {

	private CashGamePlayer entity;

	@Override
	public void init() {
		entity = valid(CashGamePlayer.class);
	}

	@Test
	void should_add_chips() {
		BigDecimal totalInvested = entity.getTotalInvested();
		BigDecimal currentStack = entity.getCurrentStack();

		entity.addChips(TEN);

		assertThat(entity.getCurrentStack()).isEqualTo(currentStack.add(TEN));
		assertThat(entity.getTotalInvested()).isEqualTo(totalInvested.add(TEN));
	}

	@Test
	void should_rebuy() {
		BigDecimal totalInvested = entity.getTotalInvested();

		entity.rebuy(TEN);

		assertThat(entity.getCurrentStack()).isEqualTo(TEN);
		assertThat(entity.getTotalInvested()).isEqualTo(totalInvested.add(TEN));
	}

	@Test
	void should_cashout() {
		BigDecimal totalInvested = entity.getTotalInvested();

		entity.cashout();

		assertThat(entity.getCurrentStack()).isEqualTo(ZERO);
		assertThat(entity.getTotalInvested()).isEqualTo(totalInvested);
	}

}
