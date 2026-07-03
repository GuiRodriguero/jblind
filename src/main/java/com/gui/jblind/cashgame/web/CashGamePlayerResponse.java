package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGamePlayer;

public record CashGamePlayerResponse(String id, String name) {

	public static CashGamePlayerResponse of(CashGamePlayer entity) {
		return new CashGamePlayerResponse(entity.getId(), entity.getName());
	}

}
