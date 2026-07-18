package com.gui.jblind.tournament;

import com.gui.jblind.tournament.web.TournamentLogRequest;
import com.gui.jblind.tournament.web.TournamentLogResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TournamentLogService {

	private final TournamentLogRepository repository;

	private final TournamentPlayerService playerService;

	public TournamentLogResponse createLog(String tournamentId, TournamentLogRequest request) {
		TournamentLog log = TournamentLog.builder()
			.tournamentId(tournamentId)
			.playerId(request.tournamentPlayerId())
			.type(request.type())
			.amount(request.amount())
			.message(request.message())
			.timestamp(LocalDateTime.now())
			.build();

		playerService.updatePlayerStats(request);

		return TournamentLogResponse.from(repository.save(log));
	}

}
