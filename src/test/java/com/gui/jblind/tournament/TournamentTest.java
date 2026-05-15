package com.gui.jblind.tournament;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static com.gui.jblind.tournament.TournamentStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentTest {

    private Tournament tournament;

    @BeforeEach
    void setUp() {
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
