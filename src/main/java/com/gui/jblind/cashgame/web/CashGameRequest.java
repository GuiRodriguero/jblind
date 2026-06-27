package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.gui.jblind.cashgame.CashGameStatus.SCHEDULED;

public record CashGameRequest(@NotBlank String name, @NotNull LocalDateTime scheduledAt, @Positive BigDecimal minBuyIn,
		@Positive BigDecimal maxBuyIn, @Positive BigDecimal smallBlind, @Positive BigDecimal bigBlind,
		List<CashGamePlayerRequest> players) {

	public CashGame to() {
		CashGame cashGame = CashGame.builder()
			.name(name())
			.scheduledAt(scheduledAt())
			.minBuyIn(minBuyIn())
			.maxBuyIn(maxBuyIn())
			.smallBlind(smallBlind())
			.bigBlind(bigBlind())
			.status(SCHEDULED)
			.build();

		players().forEach(player -> cashGame.addPlayer(player.to()));

		return cashGame;
	}

	public CashGame to(String id) {
		return to().toBuilder().id(id).build();
	}
}
