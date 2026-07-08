package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.web.CashGameLogRequest;
import com.gui.jblind.cashgame.web.CashGameLogRequestTemplateLoader;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

class CashGamePlayerServiceTest extends TestBase {

	private CashGamePlayerService service;

	private CashGamePlayer cashGamePlayer;

	@Mock
	private CashGamePlayerRepository repository;

	@Override
	public void init() {
		cashGamePlayer = valid(CashGamePlayer.class);
		service = new CashGamePlayerService(repository);
	}

	@Test
	void should_update_player_stats_buy_in() {
		CashGameLogRequest request = CashGameLogRequestTemplateLoader.buyIn();

		when(repository.findById(request.cashGamePlayerId())).thenReturn(Optional.of(cashGamePlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.cashGamePlayerId());
		inOrder.verify(repository).save(cashGamePlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_add_on() {
		CashGameLogRequest request = CashGameLogRequestTemplateLoader.addOn();

		when(repository.findById(request.cashGamePlayerId())).thenReturn(Optional.of(cashGamePlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.cashGamePlayerId());
		inOrder.verify(repository).save(cashGamePlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_rebuy() {
		CashGameLogRequest request = CashGameLogRequestTemplateLoader.rebuy();

		when(repository.findById(request.cashGamePlayerId())).thenReturn(Optional.of(cashGamePlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.cashGamePlayerId());
		inOrder.verify(repository).save(cashGamePlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_cashout() {
		CashGameLogRequest request = CashGameLogRequestTemplateLoader.cashOut();

		when(repository.findById(request.cashGamePlayerId())).thenReturn(Optional.of(cashGamePlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.cashGamePlayerId());
		inOrder.verify(repository).save(cashGamePlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_not_update_player_stats_when_player_not_found() {
		CashGameLogRequest request = CashGameLogRequestTemplateLoader.cashOut();

		when(repository.findById(request.cashGamePlayerId())).thenReturn(Optional.empty());

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.cashGamePlayerId());
		inOrder.verify(repository, never()).save(cashGamePlayer);
		inOrder.verifyNoMoreInteractions();
	}

}
