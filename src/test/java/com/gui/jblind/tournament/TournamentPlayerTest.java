package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentPlayerTest extends TestBase {

	private TournamentPlayer entity;

	@Override
	public void init() {
		entity = valid(TournamentPlayer.class);
	}

	@Test
	void should_add_entry() {
		int entries = entity.getEntries();
		BigDecimal totalInvested = entity.getTotalInvested();

		entity.addEntry(TEN);

		assertThat(entity.getEntries()).isEqualTo(entries + 1);
		assertThat(entity.getTotalInvested()).isEqualTo(totalInvested.add(TEN));
	}

	@Test
	void should_add_on() {
		BigDecimal totalInvested = entity.getTotalInvested();

		entity.addOn(TEN);

		assertThat(entity.getAddOn()).isTrue();
		assertThat(entity.getTotalInvested()).isEqualTo(totalInvested.add(TEN));
	}

	@Test
	void should_set_final_position() {
		entity.finalPosition(1);

		assertThat(entity.getFinalPosition()).isEqualTo(1);
	}

	@Test
	void should_eliminate() {
		int eliminationsMade = entity.getEliminationsMade();

		entity.eliminate();

		assertThat(entity.getEliminationsMade()).isEqualTo(eliminationsMade + 1);
	}

}
