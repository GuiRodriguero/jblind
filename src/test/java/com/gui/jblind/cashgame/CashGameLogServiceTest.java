package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.web.CashGameLogRequest;
import com.gui.jblind.cashgame.web.CashGameLogResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class CashGameLogServiceTest extends TestBase {

	private CashGameLogService service;

	@Mock
	private CashGameLogRepository repository;

	@Mock
	private CashGamePlayerService playerService;

	@Override
	public void init() {
		service = new CashGameLogService(repository, playerService);
	}

	@Test
	void should_create_log() {
		CashGameLog log = valid(CashGameLog.class);
		CashGameLogRequest request = valid(CashGameLogRequest.class);

		when(repository.save(any(CashGameLog.class))).thenReturn(log);

		assertThat(service.createLog("cashGameId", request)).isEqualTo(CashGameLogResponse.from(log));

		InOrder inOrder = inOrder(repository, playerService);
		inOrder.verify(playerService).updatePlayerStats(request);
		inOrder.verify(repository).save(any(CashGameLog.class));
		inOrder.verifyNoMoreInteractions();
	}

}
