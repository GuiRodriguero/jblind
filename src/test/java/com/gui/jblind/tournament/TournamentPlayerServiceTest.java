package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.web.TournamentLogRequest;
import com.gui.jblind.tournament.web.TournamentLogRequestTemplateLoader;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

class TournamentPlayerServiceTest extends TestBase {

	private TournamentPlayerService service;

	private TournamentPlayer tournamentPlayer;

	@Mock
	private TournamentPlayerRepository repository;

	@Override
	public void init() {
		tournamentPlayer = valid(TournamentPlayer.class);
		service = new TournamentPlayerService(repository);
	}

	@Test
	void should_update_player_stats_buy_in() {
		TournamentLogRequest request = TournamentLogRequestTemplateLoader.buyIn();

		when(repository.findById(request.tournamentPlayerId())).thenReturn(Optional.of(tournamentPlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.tournamentPlayerId());
		inOrder.verify(repository).save(tournamentPlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_add_on() {
		TournamentLogRequest request = TournamentLogRequestTemplateLoader.addOn();

		when(repository.findById(request.tournamentPlayerId())).thenReturn(Optional.of(tournamentPlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.tournamentPlayerId());
		inOrder.verify(repository).save(tournamentPlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_rebuy() {
		TournamentLogRequest request = TournamentLogRequestTemplateLoader.rebuy();

		when(repository.findById(request.tournamentPlayerId())).thenReturn(Optional.of(tournamentPlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.tournamentPlayerId());
		inOrder.verify(repository).save(tournamentPlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_elimination() {
		TournamentLogRequest request = TournamentLogRequestTemplateLoader.elimination();

		when(repository.findById(request.tournamentPlayerId())).thenReturn(Optional.of(tournamentPlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.tournamentPlayerId());
		inOrder.verify(repository).save(tournamentPlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_player_stats_left() {
		TournamentLogRequest request = TournamentLogRequestTemplateLoader.left();

		when(repository.findById(request.tournamentPlayerId())).thenReturn(Optional.of(tournamentPlayer));

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.tournamentPlayerId());
		inOrder.verify(repository).save(tournamentPlayer);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_not_update_player_stats_when_player_not_found() {
		TournamentLogRequest request = TournamentLogRequestTemplateLoader.buyIn();

		when(repository.findById(request.tournamentPlayerId())).thenReturn(Optional.empty());

		assertThatCode(() -> service.updatePlayerStats(request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(request.tournamentPlayerId());
		inOrder.verify(repository, never()).save(tournamentPlayer);
		inOrder.verifyNoMoreInteractions();
	}

}
