package com.gui.jblind.tournament;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class TournamentLogQuery {

	private final TournamentLogRepository repository;

	public List<TournamentLog> findAllByTournamentId(String tournamentId) {
		return repository.findAllByTournamentIdOrderByTimestampDesc(tournamentId);
	}

}
