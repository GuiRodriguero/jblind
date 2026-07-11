package com.gui.jblind.cashgame;

import com.gui.jblind.cashgame.web.CashGameLogRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class CashGamePlayerService {

	private final CashGamePlayerRepository playerRepository;

	void updatePlayerStats(CashGameLogRequest request) {
		playerRepository.findById(request.cashGamePlayerId()).ifPresent(player -> {
			handlePlayerSessionBankroll(player, request.type(), request.amount());
			playerRepository.save(player);
		});
	}

	private void handlePlayerSessionBankroll(CashGamePlayer player, CashGameLogType type, BigDecimal amount) {
		switch (type) {
			case BUY_IN, ADD_ON -> player.addChips(amount);
			case REBUY -> player.rebuy(amount);
			case CASHOUT -> player.cashout();
		}
	}

}
