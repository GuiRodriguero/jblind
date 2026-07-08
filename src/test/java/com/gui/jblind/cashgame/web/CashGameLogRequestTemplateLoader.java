package com.gui.jblind.cashgame.web;

import org.instancio.Instancio;

import static com.gui.jblind.cashgame.CashGameLogType.*;
import static org.instancio.Select.field;

public class CashGameLogRequestTemplateLoader {

	public static CashGameLogRequest buyIn() {
		return Instancio.of(CashGameLogRequest.class).set(field(CashGameLogRequest::type), BUY_IN).create();
	}

	public static CashGameLogRequest rebuy() {
		return Instancio.of(CashGameLogRequest.class).set(field(CashGameLogRequest::type), REBUY).create();
	}

	public static CashGameLogRequest addOn() {
		return Instancio.of(CashGameLogRequest.class).set(field(CashGameLogRequest::type), ADD_ON).create();
	}

	public static CashGameLogRequest cashOut() {
		return Instancio.of(CashGameLogRequest.class).set(field(CashGameLogRequest::type), CASHOUT).create();
	}

}
