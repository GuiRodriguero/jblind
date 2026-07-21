package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class CashGameLogQueryTest extends TestBase {

	private CashGameLogQuery query;

	@Mock
	private CashGameLogRepository repository;

	@Override
	public void init() {
		query = new CashGameLogQuery(repository);
	}

	@Test
	void should_find_all_by_cash_game_id() {
		String cashGameId = valid(String.class);
		List<CashGameLog> logs = valid(CashGameLog.class, 3);

		when(repository.findAllByCashGameIdOrderByTimestampDesc(cashGameId)).thenReturn(logs);

		assertThat(query.findAllByCashGameId(cashGameId)).isEqualTo(logs);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAllByCashGameIdOrderByTimestampDesc(cashGameId);
		inOrder.verifyNoMoreInteractions();
	}

}
