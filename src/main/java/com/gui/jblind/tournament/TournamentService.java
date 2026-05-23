package com.gui.jblind.tournament;

import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import com.gui.jblind.tournament.web.TournamentCreateRequest;
import com.gui.jblind.tournament.web.TournamentDetailResponse;
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

	public Long createTournament(TournamentCreateRequest request) {
		return repository.save(TournamentCreateRequest.to(request)).getId();
	}

	@Transactional(readOnly = true)
	public List<TournamentSummaryResponse> listAllTournaments() {
		return repository.findAll().stream().map(TournamentSummaryResponse::of).toList();
	}

	@Transactional(readOnly = true)
	public TournamentDetailResponse getTournamentById(Long id) {
		return repository.findById(id)
			.map(TournamentDetailResponse::of)
			.orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));
	}

	public void playTournament(Long id) {
		Tournament tournament = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));

		if (tournament.getStatus() == FINISHED) {
			throw new BusinessException("Cannot start a tournament that is already finished.");
		}

		repository.save(tournament.startTournament());
	}

}
