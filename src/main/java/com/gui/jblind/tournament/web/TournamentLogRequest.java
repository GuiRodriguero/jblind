package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLogType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TournamentLogRequest(String tournamentPlayerId, @NotNull TournamentLogType type,
		@NotNull @PositiveOrZero BigDecimal amount, String message, Integer finalPosition) {
}