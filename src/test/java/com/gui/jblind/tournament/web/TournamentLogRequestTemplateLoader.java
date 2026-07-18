package com.gui.jblind.tournament.web;

import org.instancio.Instancio;

import static com.gui.jblind.tournament.TournamentLogType.*;
import static org.instancio.Select.field;

public class TournamentLogRequestTemplateLoader {

	public static TournamentLogRequest buyIn() {
		return Instancio.of(TournamentLogRequest.class).set(field(TournamentLogRequest::type), BUY_IN).create();
	}

	public static TournamentLogRequest rebuy() {
		return Instancio.of(TournamentLogRequest.class).set(field(TournamentLogRequest::type), REBUY).create();
	}

	public static TournamentLogRequest addOn() {
		return Instancio.of(TournamentLogRequest.class).set(field(TournamentLogRequest::type), ADD_ON).create();
	}

	public static TournamentLogRequest elimination() {
		return Instancio.of(TournamentLogRequest.class).set(field(TournamentLogRequest::type), ELIMINATION).create();
	}

	public static TournamentLogRequest left() {
		return Instancio.of(TournamentLogRequest.class).set(field(TournamentLogRequest::type), LEFT).create();
	}

}
