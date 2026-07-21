package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class TournamentLogQueryTest extends TestBase {

	private TournamentLogQuery query;

	@Mock
	private TournamentLogRepository repository;

	@Override
	public void init() {
		query = new TournamentLogQuery(repository);
	}

	@Test
	void should_find_all_by_tournament_id() {
		String tournament = valid(String.class);
		List<TournamentLog> logs = valid(TournamentLog.class, 3);

		when(repository.findAllByTournamentIdOrderByTimestampDesc(tournament)).thenReturn(logs);

		assertThat(query.findAllByTournamentId(tournament)).isEqualTo(logs);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAllByTournamentIdOrderByTimestampDesc(tournament);
		inOrder.verifyNoMoreInteractions();
	}

}
