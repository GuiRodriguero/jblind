package com.gui.jblind.cashgame;

import org.instancio.Instancio;

import static com.gui.jblind.cashgame.CashGameStatus.*;
import static org.instancio.Select.field;

class CashGameTemplateLoader {

	static CashGame scheduled() {
		return Instancio.of(CashGame.class).set(field(CashGame::getStatus), SCHEDULED).create();
	}

	static CashGame inProgress() {
		return Instancio.of(CashGame.class).set(field(CashGame::getStatus), IN_PROGRESS).create();
	}

	static CashGame finished() {
		return Instancio.of(CashGame.class).set(field(CashGame::getStatus), FINISHED).create();
	}

}
