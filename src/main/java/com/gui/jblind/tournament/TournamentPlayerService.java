package com.gui.jblind.tournament;

import com.gui.jblind.tournament.web.TournamentLogRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class TournamentPlayerService {

	private final TournamentPlayerRepository repository;

	void updatePlayerStats(TournamentLogRequest request) {
		if (request.tournamentPlayerId() == null) {
			return;
		}
		repository.findById(request.tournamentPlayerId()).ifPresent(player -> {
			handlePlayerSessionBankroll(player, request.type(), request.amount(), request.finalPosition());
			repository.save(player);
		});
	}

	private void handlePlayerSessionBankroll(TournamentPlayer player, TournamentLogType type, BigDecimal amount,
			Integer finalPosition) {
		switch (type) {
			case ADD_ON -> player.addOn(amount);
			case BUY_IN, REBUY -> player.addEntry(amount);
			case ELIMINATION -> player.eliminate();
			case LEFT -> player.finalPosition(finalPosition);
		}
	}

}
