package com.gui.jblind.tournament;

import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import com.gui.jblind.tournament.web.TournamentDetailResponse;
import com.gui.jblind.tournament.web.TournamentRequest;
import com.gui.jblind.tournament.web.TournamentSummaryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gui.jblind.tournament.TournamentStatus.FINISHED;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TournamentService {

	private final TournamentRepository repository;

	public String createTournament(TournamentRequest request) {
		return repository.save(request.to()).getId();
	}

	@Transactional(readOnly = true)
	public List<TournamentSummaryResponse> listAllTournaments() {
		return repository.findAll().stream().map(TournamentSummaryResponse::of).toList();
	}

	@Transactional(readOnly = true)
	public TournamentDetailResponse getTournamentById(String id) {
		return repository.findById(id)
			.map(TournamentDetailResponse::of)
			.orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));
	}

	public void playTournament(String id) {
		Tournament tournament = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));

		if (tournament.getStatus() == FINISHED) {
			throw new BusinessException("Cannot start a tournament that is already finished.");
		}

		repository.save(tournament.startTournament());
	}

	public void deleteTournament(String id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Tournament not found with id: " + id);
		}

		repository.deleteById(id);
	}

	public void updateTournament(String id, TournamentRequest request) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Tournament not found with id: " + id);
		}

		repository.save(request.to(id));
	}

}
