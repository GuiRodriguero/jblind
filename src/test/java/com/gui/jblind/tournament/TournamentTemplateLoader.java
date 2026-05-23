package com.gui.jblind.tournament;

import org.instancio.Instancio;

import static com.gui.jblind.tournament.TournamentStatus.FINISHED;
import static com.gui.jblind.tournament.TournamentStatus.SCHEDULED;
import static org.instancio.Select.field;

class TournamentTemplateLoader {

	static Tournament scheduled() {
		return Instancio.of(Tournament.class).set(field(Tournament::getStatus), SCHEDULED).create();
	}

	static Tournament finished() {
		return Instancio.of(Tournament.class).set(field(Tournament::getStatus), FINISHED).create();
	}

}
