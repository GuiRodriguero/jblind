package com.gui.jblind.cashgame;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.web.*;
import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static com.gui.jblind.cashgame.CashGameStatus.FINISHED;
import static com.gui.jblind.cashgame.CashGameStatus.IN_PROGRESS;
import static com.gui.jblind.cashgame.CashGameTemplateLoader.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class CashGameServiceTest extends TestBase {

	private static final String CASH_GAME_ID = randomUUID().toString();

	private CashGameService service;

	@Mock
	private CashGameQuery query;

	@Mock
	private CashGameRepository repository;

	@Mock
	private CashGameLogQuery logQuery;

	@Override
	public void init() {
		service = new CashGameService(query, repository, logQuery);
	}

	@Test
	void should_create_cash_game() {
		CashGameRequest request = valid(CashGameRequest.class);
		CashGame cashGame = request.to();

		when(repository.save(cashGame)).thenReturn(cashGame);

		assertThat(service.createCashGame(request)).isEqualTo(cashGame.getId());

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).save(cashGame);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_list_all_cash_games() {
		CashGame cashGame = valid(CashGame.class);
		when(repository.findAll()).thenReturn(List.of(cashGame));

		List<CashGameSummaryResponse> result = service.listAllCashGames();

		assertThat(result).containsExactly(CashGameSummaryResponse.of(cashGame));

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_return_empty_list_when_no_cash_games_found() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<CashGameSummaryResponse> result = service.listAllCashGames();

		assertThat(result).isEmpty();

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_get_cash_game_by_id() {
		CashGame cashGame = valid(CashGame.class);
		List<CashGameLog> logs = valid(CashGameLog.class, 3);

		when(query.findById(CASH_GAME_ID)).thenReturn(cashGame);
		when(logQuery.findAllByCashGameId(CASH_GAME_ID)).thenReturn(logs);

		CashGameDetailResponse result = service.getCashGameById(CASH_GAME_ID);

		assertThat(result)
			.isEqualTo(CashGameDetailResponse.of(cashGame, logs.stream().map(CashGameLogResponse::from).toList()));

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(CASH_GAME_ID);
		inOrder.verify(logQuery).findAllByCashGameId(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_play_cash_game() {
		CashGame cashGame = scheduled();
		when(query.findById(CASH_GAME_ID)).thenReturn(cashGame);
		when(repository.save(cashGame)).thenReturn(cashGame);

		service.playCashGame(CASH_GAME_ID);

		assertThat(cashGame.getStatus()).isEqualTo(IN_PROGRESS);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(CASH_GAME_ID);
		inOrder.verify(repository).save(cashGame);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_finish_cash_game() {
		CashGame cashGame = inProgress();
		when(query.findById(CASH_GAME_ID)).thenReturn(cashGame);
		when(repository.save(cashGame)).thenReturn(cashGame);

		service.finishCashGame(CASH_GAME_ID);

		assertThat(cashGame.getStatus()).isEqualTo(FINISHED);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(CASH_GAME_ID);
		inOrder.verify(repository).save(cashGame);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_finishing_cash_game_not_in_progress() {
		CashGame cashGame = scheduled();
		when(query.findById(CASH_GAME_ID)).thenReturn(cashGame);

		assertThatThrownBy(() -> service.finishCashGame(CASH_GAME_ID)).isInstanceOf(BusinessException.class)
			.hasMessage("Cannot finish a cash game that is not in progress.");

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_playing_already_finished_cash_game() {
		CashGame cashGame = finished();
		when(query.findById(CASH_GAME_ID)).thenReturn(cashGame);

		assertThatThrownBy(() -> service.playCashGame(CASH_GAME_ID)).isInstanceOf(BusinessException.class)
			.hasMessage("Cannot start a cash game that is already finished.");

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_delete_cash_game() {
		when(repository.existsById(CASH_GAME_ID)).thenReturn(true);

		service.deleteCashGame(CASH_GAME_ID);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verify(repository).deleteById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_deleting_non_existent_cash_game() {
		when(repository.existsById(CASH_GAME_ID)).thenReturn(false);

		assertThatThrownBy(() -> service.deleteCashGame(CASH_GAME_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Cash Game not found with id: " + CASH_GAME_ID);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_cash_game() {
		CashGameRequest request = valid(CashGameRequest.class);
		when(repository.existsById(CASH_GAME_ID)).thenReturn(true);

		assertThatCode(() -> service.updateCashGame(CASH_GAME_ID, request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(query, repository, logQuery);
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

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_add_player() {
		CashGame cashGame = valid(CashGame.class);
		CashGamePlayerRequest playerRequest = valid(CashGamePlayerRequest.class);
		when(query.findById(CASH_GAME_ID)).thenReturn(cashGame);

		CashGamePlayer player = playerRequest.to();
		cashGame.addPlayer(player);

		assertThat(service.addPlayer(CASH_GAME_ID, playerRequest)).isEqualTo(CashGamePlayerResponse.of(player));

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(CASH_GAME_ID);
		inOrder.verify(repository).save(cashGame);
		inOrder.verifyNoMoreInteractions();
	}

}
