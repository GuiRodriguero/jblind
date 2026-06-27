package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGame;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashGameSummaryResponse(String id, String name, LocalDateTime scheduledAt, BigDecimal smallBlind,
		BigDecimal bigBlind, BigDecimal minBuyIn, BigDecimal maxBuyIn, Integer players, String status) {

	public static CashGameSummaryResponse of(CashGame entity) {
		return new CashGameSummaryResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getSmallBlind(), entity.getBigBlind(), entity.getMinBuyIn(), entity.getMaxBuyIn(),
				entity.getPlayers().size(), entity.getStatus().name());
	}

}
