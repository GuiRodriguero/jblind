package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGamePlayer;

import java.math.BigDecimal;

public record CashGamePlayerResponse(String id, String name, BigDecimal totalInvested, BigDecimal currentStack) {

	public static CashGamePlayerResponse of(CashGamePlayer entity) {
		return new CashGamePlayerResponse(entity.getId(), entity.getName(), entity.getTotalInvested(),
				entity.getCurrentStack());
	}

}
