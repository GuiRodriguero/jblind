package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class TournamentQueryTest extends TestBase {

	private static final String TOURNAMENT_ID = "randomId";

	private TournamentQuery query;

	@Mock
	private TournamentRepository repository;

	@Override
	public void init() {
		query = new TournamentQuery(repository);
	}

	@Test
	void should_find_by_id() {
		Tournament tournament = valid(Tournament.class);
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));

		assertThat(query.findById(TOURNAMENT_ID)).isEqualTo(tournament);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_tournament_not_found() {
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> query.findById(TOURNAMENT_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Tournament not found with id: " + TOURNAMENT_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

}
