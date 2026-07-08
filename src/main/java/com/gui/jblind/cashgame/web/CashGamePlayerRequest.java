package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGamePlayer;

public record CashGamePlayerRequest(String name) {

	public CashGamePlayer to() {
		return CashGamePlayer.builder().name(name()).build();
	}

}
