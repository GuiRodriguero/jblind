package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGame;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CashGameDetailResponse(String id, String name, LocalDateTime scheduledAt, BigDecimal minBuyIn,
		BigDecimal maxBuyIn, BigDecimal smallBlind, BigDecimal bigBlind, String status,
		List<CashGamePlayerResponse> players) {

	public static CashGameDetailResponse of(CashGame entity) {
		return new CashGameDetailResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getMinBuyIn(), entity.getMaxBuyIn(), entity.getSmallBlind(), entity.getBigBlind(),
				entity.getStatus().name(), entity.getPlayers().stream().map(CashGamePlayerResponse::of).toList());
	}

}
