package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.Tournament;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.gui.jblind.tournament.TournamentStatus.SCHEDULED;

public record TournamentCreateRequest(@NotBlank String name, @NotNull LocalDateTime scheduledAt,
		@Positive Integer expectedPlayers, @Positive BigDecimal buyIn, @Positive Integer startingStack,
		boolean allowRebuys, boolean allowAddOn, List<TournamentLevelRequest> levels) {

	public static Tournament to(TournamentCreateRequest request) {
		Tournament tournament = Tournament.builder()
			.name(request.name())
			.scheduledAt(request.scheduledAt())
			.expectedPlayers(request.expectedPlayers())
			.buyIn(request.buyIn())
			.startingStack(request.startingStack())
			.allowRebuys(request.allowRebuys())
			.allowAddOn(request.allowAddOn())
			.status(SCHEDULED)
			.build();

		request.levels().forEach(level -> tournament.addLevel(TournamentLevelRequest.to(level)));

		return tournament;
	}
}