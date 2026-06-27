package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.web.CashGameDetailResponse;
import com.gui.jblind.cashgame.web.CashGameRequest;
import com.gui.jblind.cashgame.web.CashGameSummaryResponse;
import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gui.jblind.cashgame.CashGameStatus.IN_PROGRESS;
import static com.gui.jblind.cashgame.CashGameTemplateLoader.finished;
import static com.gui.jblind.cashgame.CashGameTemplateLoader.scheduled;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class CashGameServiceTest extends TestBase {

	private static final String CASH_GAME_ID = randomUUID().toString();

	private CashGameService service;

	@Mock
	private CashGameRepository repository;

	@Override
	public void init() {
		service = new CashGameService(repository);
	}

	@Test
	void should_create_cash_game() {
		CashGameRequest request = valid(CashGameRequest.class);
		CashGame cashGame = request.to();

		when(repository.save(cashGame)).thenReturn(cashGame);

		assertThat(service.createCashGame(request)).isEqualTo(cashGame.getId());

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).save(cashGame);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_list_all_cash_games() {
		CashGame cashGame = valid(CashGame.class);
		when(repository.findAll()).thenReturn(List.of(cashGame));

		List<CashGameSummaryResponse> result = service.listAllCashGames();

		assertThat(result).containsExactly(CashGameSummaryResponse.of(cashGame));

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_return_empty_list_when_no_cash_games_found() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<CashGameSummaryResponse> result = service.listAllCashGames();

		assertThat(result).isEmpty();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_get_cash_game_by_id() {
		CashGame cashGame = valid(CashGame.class);
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.of(cashGame));

		CashGameDetailResponse result = service.getCashGameById(CASH_GAME_ID);

		assertThat(result).isEqualTo(CashGameDetailResponse.of(cashGame));

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_cash_game_not_found_by_id() {
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getCashGameById(CASH_GAME_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Cash Game not found with id: " + CASH_GAME_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_play_cash_game() {
		CashGame cashGame = scheduled();
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.of(cashGame));
		when(repository.save(cashGame)).thenReturn(cashGame);

		service.playCashGame(CASH_GAME_ID);

		assertThat(cashGame.getStatus()).isEqualTo(IN_PROGRESS);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verify(repository).save(cashGame);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_playing_non_existent_cash_game() {
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.playCashGame(CASH_GAME_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Cash Game not found with id: " + CASH_GAME_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_playing_already_finished_cash_game() {
		CashGame cashGame = finished();
		when(repository.findById(CASH_GAME_ID)).thenReturn(Optional.of(cashGame));

		assertThatThrownBy(() -> service.playCashGame(CASH_GAME_ID)).isInstanceOf(BusinessException.class)
			.hasMessage("Cannot start a cash game that is already finished.");

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_delete_cash_game() {
		when(repository.existsById(CASH_GAME_ID)).thenReturn(true);

		service.deleteCashGame(CASH_GAME_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verify(repository).deleteById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_deleting_non_existent_cash_game() {
		when(repository.existsById(CASH_GAME_ID)).thenReturn(false);

		assertThatThrownBy(() -> service.deleteCashGame(CASH_GAME_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Cash Game not found with id: " + CASH_GAME_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_cash_game() {
		CashGameRequest request = valid(CashGameRequest.class);
		when(repository.existsById(CASH_GAME_ID)).thenReturn(true);

		assertThatCode(() -> service.updateCashGame(CASH_GAME_ID, request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verify(repository).save(request.to(CASH_GAME_ID));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_updating_non_existent_cash_game() {
		CashGameRequest request = valid(CashGameRequest.class);
		when(repository.existsById(CASH_GAME_ID)).thenReturn(false);

		assertThatThrownBy(() -> service.updateCashGame(CASH_GAME_ID, request))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Cash Game not found with id: " + CASH_GAME_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

}
