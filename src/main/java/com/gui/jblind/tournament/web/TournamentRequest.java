package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.gui.jblind.tournament.TournamentStatus.SCHEDULED;

public record TournamentRequest(@NotBlank String name, @NotNull LocalDateTime scheduledAt,
		@Positive Integer expectedPlayers, @Positive BigDecimal buyIn, @Positive Integer startingStack,
		boolean allowRebuys, boolean allowAddOn, List<TournamentLevelRequest> levels,
		List<TournamentPlayerRequest> players, TournamentPrizeRequest prize) {

	public Tournament to() {
		Tournament tournament = Tournament.builder()
			.name(name)
			.scheduledAt(scheduledAt)
			.expectedPlayers(expectedPlayers)
			.buyIn(buyIn)
			.startingStack(startingStack)
			.allowRebuys(allowRebuys)
			.allowAddOn(allowAddOn)
			.prize(prize.to())
			.status(SCHEDULED)
			.build();

		levels.forEach(level -> tournament.addLevel(level.to()));
		players.forEach(player -> tournament.addPlayer(player.to()));

		return tournament;
	}

	public Tournament to(String id) {
		return to().toBuilder().id(id).build();
	}
}