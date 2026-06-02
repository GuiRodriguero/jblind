package com.gui.jblind.tournament;

import com.gui.jblind.TestBase;
import com.gui.jblind.core.exception.BusinessException;
import com.gui.jblind.core.exception.ResourceNotFoundException;
import com.gui.jblind.tournament.web.TournamentCreateRequest;
import com.gui.jblind.tournament.web.TournamentDetailResponse;
import com.gui.jblind.tournament.web.TournamentSummaryResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gui.jblind.tournament.TournamentStatus.IN_PROGRESS;
import static com.gui.jblind.tournament.TournamentTemplateLoader.finished;
import static com.gui.jblind.tournament.TournamentTemplateLoader.scheduled;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class TournamentServiceTest extends TestBase {

	private static final String TOURNAMENT_ID = randomUUID().toString();

	private TournamentService service;

	@Mock
	private TournamentRepository repository;

	@Override
	public void init() {
		service = new TournamentService(repository);
	}

	@Test
	void should_create_tournament() {
		TournamentCreateRequest request = valid(TournamentCreateRequest.class);
		Tournament tournament = TournamentCreateRequest.to(request);

		when(repository.save(tournament)).thenReturn(tournament);

		assertThat(service.createTournament(request)).isEqualTo(tournament.getId());

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).save(tournament);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_list_all_tournaments() {
		Tournament tournament = valid(Tournament.class);
		when(repository.findAll()).thenReturn(List.of(tournament));

		List<TournamentSummaryResponse> result = service.listAllTournaments();

		assertThat(result).containsExactly(TournamentSummaryResponse.of(tournament));

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_return_empty_list_when_no_tournaments_found() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<TournamentSummaryResponse> result = service.listAllTournaments();

		assertThat(result).isEmpty();

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_get_tournament_by_id() {
		Tournament tournament = valid(Tournament.class);
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));

		TournamentDetailResponse result = service.getTournamentById(TOURNAMENT_ID);

		assertThat(result).isEqualTo(TournamentDetailResponse.of(tournament));

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_tournament_not_found_by_id() {
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getTournamentById(TOURNAMENT_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Tournament not found with id: " + TOURNAMENT_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_play_tournament() {
		Tournament tournament = scheduled();
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));
		when(repository.save(tournament)).thenReturn(tournament);

		service.playTournament(TOURNAMENT_ID);

		assertThat(tournament.getStatus()).isEqualTo(IN_PROGRESS);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verify(repository).save(tournament);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_playing_non_existent_tournament() {
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.playTournament(TOURNAMENT_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Tournament not found with id: " + TOURNAMENT_ID);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_exception_when_playing_already_finished_tournament() {
		Tournament tournament = finished();
		when(repository.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));

		assertThatThrownBy(() -> service.playTournament(TOURNAMENT_ID)).isInstanceOf(BusinessException.class)
			.hasMessage("Cannot start a tournament that is already finished.");

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

}
