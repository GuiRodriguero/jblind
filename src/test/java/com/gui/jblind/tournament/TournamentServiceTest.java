package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import com.gui.jblind.tournament.web.TournamentDetailResponse;
import com.gui.jblind.tournament.web.TournamentLogResponse;
import com.gui.jblind.tournament.web.TournamentRequest;
import com.gui.jblind.tournament.web.TournamentSummaryResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static com.gui.jblind.tournament.TournamentStatus.FINISHED;
import static com.gui.jblind.tournament.TournamentStatus.IN_PROGRESS;
import static com.gui.jblind.tournament.TournamentTemplateLoader.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class TournamentServiceTest extends TestBase {

	private static final String TOURNAMENT_ID = randomUUID().toString();

	private TournamentService service;

	@Mock
	private TournamentQuery query;

	@Mock
	private TournamentRepository repository;

	@Mock
	private TournamentLogQuery logQuery;

	@Override
	public void init() {
		service = new TournamentService(query, repository, logQuery);
	}

	@Test
	void should_create_tournament() {
		TournamentRequest request = valid(TournamentRequest.class);
		Tournament tournament = request.to();

		when(repository.save(tournament)).thenReturn(tournament);

		assertThat(service.createTournament(request)).isEqualTo(tournament.getId());

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).save(tournament);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_list_all_tournaments() {
		Tournament tournament = valid(Tournament.class);
		when(repository.findAll()).thenReturn(List.of(tournament));

		List<TournamentSummaryResponse> result = service.listAllTournaments();

		assertThat(result).containsExactly(TournamentSummaryResponse.of(tournament));

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_return_empty_list_when_no_tournaments_found() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<TournamentSummaryResponse> result = service.listAllTournaments();

		assertThat(result).isEmpty();

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_get_tournament_by_id() {
		Tournament tournament = valid(Tournament.class);
		List<TournamentLog> logs = valid(TournamentLog.class, 3);

		when(query.findById(TOURNAMENT_ID)).thenReturn(tournament);
		when(logQuery.findAllByTournamentId(TOURNAMENT_ID)).thenReturn(logs);

		TournamentDetailResponse result = service.getTournamentById(TOURNAMENT_ID);

		assertThat(result).isEqualTo(
				TournamentDetailResponse.of(tournament, logs.stream().map(TournamentLogResponse::from).toList()));

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(TOURNAMENT_ID);
		inOrder.verify(logQuery).findAllByTournamentId(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_play_tournament() {
		Tournament tournament = scheduled();
		when(query.findById(TOURNAMENT_ID)).thenReturn(tournament);
		when(repository.save(tournament.startTournament())).thenReturn(tournament);

		assertThatCode(() -> service.playTournament(TOURNAMENT_ID)).doesNotThrowAnyException();
		assertThat(tournament.getStatus()).isEqualTo(IN_PROGRESS);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(TOURNAMENT_ID);
		inOrder.verify(repository).save(tournament.startTournament());
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_playing_already_finished_tournament() {
		Tournament tournament = finished();
		when(query.findById(TOURNAMENT_ID)).thenReturn(tournament);

		assertThatThrownBy(() -> service.playTournament(TOURNAMENT_ID)).isInstanceOf(BusinessException.class)
			.hasMessage("Cannot start a tournament that is already finished.");

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_finish_tournament() {
		Tournament tournament = inProgress();
		when(query.findById(TOURNAMENT_ID)).thenReturn(tournament);
		when(repository.save(tournament)).thenReturn(tournament);

		assertThatCode(() -> service.finishTournament(TOURNAMENT_ID)).doesNotThrowAnyException();
		assertThat(tournament.getStatus()).isEqualTo(FINISHED);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(TOURNAMENT_ID);
		inOrder.verify(repository).save(tournament.finishTournament());
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_trying_to_finish_an_in_progress_tournament() {
		Tournament tournament = scheduled();
		when(query.findById(TOURNAMENT_ID)).thenReturn(tournament);

		assertThatThrownBy(() -> service.finishTournament(TOURNAMENT_ID)).isInstanceOf(BusinessException.class)
			.hasMessage("Cannot finish a tournament that is not in progress.");

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(query).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_delete_tournament() {
		when(repository.existsById(TOURNAMENT_ID)).thenReturn(true);

		service.deleteTournament(TOURNAMENT_ID);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(TOURNAMENT_ID);
		inOrder.verify(repository).deleteById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_deleting_non_existent_tournament() {
		when(repository.existsById(TOURNAMENT_ID)).thenReturn(false);

		assertThatThrownBy(() -> service.deleteTournament(TOURNAMENT_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Tournament not found with id: " + TOURNAMENT_ID);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_update_tournament() {
		TournamentRequest request = valid(TournamentRequest.class);
		when(repository.existsById(TOURNAMENT_ID)).thenReturn(true);

		assertThatCode(() -> service.updateTournament(TOURNAMENT_ID, request)).doesNotThrowAnyException();

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(TOURNAMENT_ID);
		inOrder.verify(repository).save(request.to(TOURNAMENT_ID));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_updating_non_existent_tournament() {
		TournamentRequest request = valid(TournamentRequest.class);
		when(repository.existsById(TOURNAMENT_ID)).thenReturn(false);

		assertThatThrownBy(() -> service.updateTournament(TOURNAMENT_ID, request))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Tournament not found with id: " + TOURNAMENT_ID);

		InOrder inOrder = inOrder(query, repository, logQuery);
		inOrder.verify(repository).existsById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

}
