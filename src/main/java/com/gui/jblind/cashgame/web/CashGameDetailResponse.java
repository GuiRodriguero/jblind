package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGame;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashGameDetailResponse(String id, String name, LocalDateTime scheduledAt, BigDecimal minBuyIn,
		BigDecimal maxBuyIn, BigDecimal smallBlind, BigDecimal bigBlind, String status) {

	public static CashGameDetailResponse of(CashGame entity) {
		return new CashGameDetailResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getMinBuyIn(), entity.getMaxBuyIn(), entity.getSmallBlind(), entity.getBigBlind(),
				entity.getStatus().name());
	}

}
