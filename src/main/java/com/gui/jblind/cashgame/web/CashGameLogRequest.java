package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGameLogType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CashGameLogRequest(String cashGamePlayerId, @NotNull CashGameLogType type,
		@NotNull @PositiveOrZero BigDecimal amount, String message) {
}