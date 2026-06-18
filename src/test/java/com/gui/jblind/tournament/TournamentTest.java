package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.tournament.TournamentStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentTest extends TestBase {

	private Tournament tournament;

	@Override
	public void init() {
		tournament = valid(Tournament.class);
	}

	@Test
	void should_add_level() {
		TournamentLevel level = valid(TournamentLevel.class);

		tournament.addLevel(level);

		assertThat(tournament.getLevels()).contains(level);
	}

	@Test
	void should_start_tournament() {
		assertThat(tournament.startTournament().getStatus()).isEqualTo(IN_PROGRESS);
	}

}
