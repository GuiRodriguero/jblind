package com.gui.jblind.tournament;

import org.instancio.Instancio;

import static com.gui.jblind.tournament.TournamentStatus.*;
import static org.instancio.Select.field;

class TournamentTemplateLoader {

	static Tournament scheduled() {
		return Instancio.of(Tournament.class).set(field(Tournament::getStatus), SCHEDULED).create();
	}

	static Tournament inProgress() {
		return Instancio.of(Tournament.class).set(field(Tournament::getStatus), IN_PROGRESS).create();
	}

	static Tournament finished() {
		return Instancio.of(Tournament.class).set(field(Tournament::getStatus), FINISHED).create();
	}

}
