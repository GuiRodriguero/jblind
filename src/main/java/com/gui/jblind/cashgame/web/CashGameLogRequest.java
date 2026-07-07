package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGameLogType;

import java.math.BigDecimal;

public record CashGameLogRequest(String cashGamePlayerId, CashGameLogType type, BigDecimal amount, String message) {
}