package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGamePlayer;

import java.math.BigDecimal;

record CashGamePlayerRequest(String name, BigDecimal buyIn) {

	public CashGamePlayer to() {
		return CashGamePlayer.builder().name(name()).build();
	}

}
