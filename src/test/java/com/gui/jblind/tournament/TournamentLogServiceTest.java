package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.web.TournamentLogRequest;
import com.gui.jblind.tournament.web.TournamentLogResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class TournamentLogServiceTest extends TestBase {

	private TournamentLogService service;

	@Mock
	private TournamentLogRepository repository;

	@Mock
	private TournamentPlayerService playerService;

	@Override
	public void init() {
		service = new TournamentLogService(repository, playerService);
	}

	@Test
	void should_create_log() {
		TournamentLog log = valid(TournamentLog.class);
		TournamentLogRequest request = valid(TournamentLogRequest.class);

		when(repository.save(any(TournamentLog.class))).thenReturn(log);

		assertThat(service.createLog("tournamentId", request)).isEqualTo(TournamentLogResponse.from(log));

		InOrder inOrder = inOrder(repository, playerService);
		inOrder.verify(playerService).updatePlayerStats(request);
		inOrder.verify(repository).save(any(TournamentLog.class));
		inOrder.verifyNoMoreInteractions();
	}

}
