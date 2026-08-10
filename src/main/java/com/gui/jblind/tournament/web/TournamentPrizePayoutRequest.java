package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrizePayout;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TournamentPrizePayoutRequest(@NotNull @Positive Integer position,
		@NotNull @PositiveOrZero BigDecimal value, @NotNull @PositiveOrZero BigDecimal percentage) {

	public TournamentPrizePayout to() {
		return TournamentPrizePayout.builder().position(position).value(value).percentage(percentage).build();
	}

	public TournamentPrizePayout to(String id) {
		return to().toBuilder().id(id).build();
	}

}
