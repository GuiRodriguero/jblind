package com.gui.jblind.cashgame;

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

class CashGameQueryTest extends TestBase {

	private static final String CASH_GAME_ID = "randomId";

	private CashGameQuery query;

	@Mock
	private CashGameRepository repository;

	@Override
	public void init() {
		query = new CashGameQuery(repository);
	}

	@Test
	void should_find_by_id() {
		CashGame tournament = valid(CashGame.class);
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.of(tournament));

		assertThat(query.findById(CASH_GAME_ID)).isEqualTo(tournament);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_tournament_not_found() {
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> query.findById(CASH_GAME_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Cash Game not found with id: " + CASH_GAME_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

}
