package com.gui.jblind.cashgame;

import com.gui.jblind.cashgame.web.*;
import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gui.jblind.cashgame.CashGameStatus.FINISHED;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CashGameService {

	private final CashGameRepository repository;

	private final CashGameLogQuery logQuery;

	public String createCashGame(CashGameRequest request) {
		return repository.save(request.to()).getId();
	}

	@Transactional(readOnly = true)
	public List<CashGameSummaryResponse> listAllCashGames() {
		return repository.findAll().stream().map(CashGameSummaryResponse::of).toList();
	}

	@Transactional(readOnly = true)
	public CashGameDetailResponse getCashGameById(String id) {
		CashGame cashGame = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cash Game not found with id: " + id));

		return CashGameDetailResponse.of(cashGame,
				logQuery.findAllByCashGameId(id).stream().map(CashGameLogResponse::from).toList());
	}

	public void playCashGame(String id) {
		CashGame cashGame = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cash Game not found with id: " + id));

		if (cashGame.getStatus() == FINISHED) {
			throw new BusinessException("Cannot start a cash game that is already finished.");
		}

		repository.save(cashGame.start());
	}

	public void deleteCashGame(String id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Cash Game not found with id: " + id);
		}

		repository.deleteById(id);
	}

	public void updateCashGame(String id, CashGameRequest request) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Cash Game not found with id: " + id);
		}

		repository.save(request.to(id));
	}

	public CashGamePlayerResponse addPlayer(String id, CashGamePlayerRequest request) {
		CashGame cashGame = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cash Game not found with id: " + id));

		CashGamePlayer player = request.to();
		cashGame.addPlayer(player);
		repository.save(cashGame);

		return CashGamePlayerResponse.of(player);
	}

}
