package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.PrizeMode;
import com.gui.jblind.tournament.Tournament;
import com.gui.jblind.tournament.TournamentLevel;
import com.gui.jblind.tournament.TournamentPlayer;
import com.gui.jblind.tournament.TournamentPrize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TournamentDetailResponse(String id, String name, LocalDateTime scheduledAt, Integer expectedPlayers,
		BigDecimal buyIn, Integer startingStack, String status, boolean allowRebuys, boolean allowAddOn,
		List<TournamentLevelResponse> levels, List<TournamentPlayerResponse> players, TournamentPrizeResponse prize,
		List<TournamentLogResponse> logs) {

	public static TournamentDetailResponse of(Tournament entity, List<TournamentLogResponse> logs) {
		return new TournamentDetailResponse(entity.getId(), entity.getName(), entity.getScheduledAt(),
				entity.getExpectedPlayers(), entity.getBuyIn(), entity.getStartingStack(), entity.getStatus().name(),
				entity.isAllowRebuys(), entity.isAllowAddOn(),
				entity.getLevels().stream().map(TournamentLevelResponse::of).toList(),
				entity.getPlayers().stream().map(TournamentPlayerResponse::of).toList(),
				TournamentPrizeResponse.of(entity.getPrize()), logs);
	}

	public record TournamentLevelResponse(Integer roundNumber, Integer smallBlind, Integer bigBlind, Integer ante,
			Integer durationInMinutes, boolean isBreak, boolean shouldColorUp) {

		public static TournamentLevelResponse of(TournamentLevel entity) {
			return new TournamentLevelResponse(entity.getRoundNumber(), entity.getSmallBlind(), entity.getBigBlind(),
					entity.getAnte(), entity.getDurationInMinutes(), entity.isBreak(), entity.isShouldColorUp());
		}

	}

	public record TournamentPlayerResponse(String id, String name) {

		public static TournamentPlayerResponse of(TournamentPlayer entity) {
			return new TournamentPlayerResponse(entity.getId(), entity.getName());
		}

	}

	public record TournamentPrizeResponse(PrizeMode mode, List<TournamentPrizePayoutResponse> payouts) {

		public static TournamentPrizeResponse of(TournamentPrize entity) {
			if (entity == null) {
				return null;
			}

			return new TournamentPrizeResponse(entity.getMode(),
					entity.getPayouts().stream().map(TournamentPrizePayoutResponse::of).toList());
		}

	}

}