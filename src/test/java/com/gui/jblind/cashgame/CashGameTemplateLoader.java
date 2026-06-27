package com.gui.jblind.cashgame;

import org.instancio.Instancio;

import static com.gui.jblind.cashgame.CashGameStatus.FINISHED;
import static com.gui.jblind.cashgame.CashGameStatus.SCHEDULED;
import static org.instancio.Select.field;

class CashGameTemplateLoader {

	static CashGame scheduled() {
		return Instancio.of(CashGame.class).set(field(CashGame::getStatus), SCHEDULED).create();
	}

	static CashGame finished() {
		return Instancio.of(CashGame.class).set(field(CashGame::getStatus), FINISHED).create();
	}

}
