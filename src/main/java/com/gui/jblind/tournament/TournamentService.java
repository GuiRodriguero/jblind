package com.gui.jblind.tournament;

import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import com.gui.jblind.tournament.web.TournamentDetailResponse;
import com.gui.jblind.tournament.web.TournamentLogResponse;
import com.gui.jblind.tournament.web.TournamentRequest;
import com.gui.jblind.tournament.web.TournamentSummaryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gui.jblind.tournament.TournamentStatus.FINISHED;
import static com.gui.jblind.tournament.TournamentStatus.IN_PROGRESS;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TournamentService {

	private final TournamentQuery query;

	private final TournamentRepository repository;

	private final TournamentLogQuery logQuery;

	public String createTournament(TournamentRequest request) {
		return repository.save(request.to()).getId();
	}

	@Transactional(readOnly = true)
	public List<TournamentSummaryResponse> listAllTournaments() {
		return repository.findAll().stream().map(TournamentSummaryResponse::of).toList();
	}

	@Transactional(readOnly = true)
	public TournamentDetailResponse getTournamentById(String id) {
		return TournamentDetailResponse.of(query.findById(id),
				logQuery.findAllByTournamentId(id).stream().map(TournamentLogResponse::from).toList());
	}

	public void playTournament(String id) {
		Tournament tournament = query.findById(id);

		if (tournament.getStatus() == FINISHED) {
			throw new BusinessException("Cannot start a tournament that is already finished.");
		}

		repository.save(tournament.startTournament());
	}

	public void finishTournament(String id) {
		Tournament tournament = query.findById(id);

		if (tournament.getStatus() != IN_PROGRESS) {
			throw new BusinessException("Cannot finish a tournament that is not in progress.");
		}

		repository.save(tournament.finishTournament());
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
