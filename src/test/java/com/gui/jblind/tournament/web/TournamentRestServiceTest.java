package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.startsWith;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentRestService.class)
class TournamentRestServiceTest extends TestBase {

	private static final String TOURNAMENT_ID = randomUUID().toString();

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TournamentService service;

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_list_tournaments() throws Exception {
		TournamentSummaryResponse response = Instancio.of(TournamentSummaryResponse.class)
			.set(field(TournamentSummaryResponse::id), TOURNAMENT_ID)
			.create();
		when(service.listAllTournaments()).thenReturn(List.of(response));

		mockMvc.perform(get("/v1/tournaments"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(TOURNAMENT_ID))
			.andExpect(jsonPath("$[0].name").value(response.name()))
			.andExpect(
					jsonPath("$[0].scheduledAt").value(startsWith(response.scheduledAt().toString().substring(0, 19))))
			.andExpect(jsonPath("$[0].expectedPlayers").value(response.expectedPlayers()))
			.andExpect(jsonPath("$[0].buyIn").value(response.buyIn().doubleValue()))
			.andExpect(jsonPath("$[0].status").value(response.status()));

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).listAllTournaments();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_create_tournament() throws Exception {
		when(service.createTournament(any())).thenReturn(TOURNAMENT_ID);

		mockMvc.perform(post("/v1/tournaments/new").contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "name": "Tournament Name",
				  "scheduledAt": "2023-12-01T10:00:00",
				  "expectedPlayers": 10,
				  "buyIn": 100,
				  "startingStack": 5000,
				  "allowRebuys": true,
				  "allowAddOn": false,
				  "levels": []
				}
				""")).andExpect(status().isCreated());

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).createTournament(any()); // TODO: Use real object?
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_get_tournament_by_id() throws Exception {
		TournamentDetailResponse response = Instancio.of(TournamentDetailResponse.class)
			.set(field(TournamentDetailResponse::id), TOURNAMENT_ID)
			.create();
		when(service.getTournamentById(TOURNAMENT_ID)).thenReturn(response);

		mockMvc.perform(get("/v1/tournaments/" + TOURNAMENT_ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(TOURNAMENT_ID))
			.andExpect(jsonPath("$.name").value(response.name()))
			.andExpect(jsonPath("$.buyIn").value(response.buyIn().doubleValue()));

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).getTournamentById(TOURNAMENT_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_play_tournament() throws Exception {
		mockMvc.perform(post("/v1/tournaments/" + TOURNAMENT_ID + "/play")).andExpect(status().isNoContent());

		verify(service).playTournament(TOURNAMENT_ID);
	}

	@Test
	void should_finish_tournament() throws Exception {
		mockMvc.perform(post("/v1/tournaments/" + TOURNAMENT_ID + "/finish")).andExpect(status().isNoContent());

		verify(service).finishTournament(TOURNAMENT_ID);
	}

	@Test
	void should_delete_tournament() throws Exception {
		mockMvc.perform(delete("/v1/tournaments/" + TOURNAMENT_ID)).andExpect(status().isSeeOther());

		verify(service).deleteTournament(TOURNAMENT_ID);
	}

	@Test
	void should_update_tournament() throws Exception {
		mockMvc.perform(put("/v1/tournaments/" + TOURNAMENT_ID).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "name": "Updated Tournament",
				  "scheduledAt": "2023-12-01T10:00:00",
				  "expectedPlayers": 15,
				  "buyIn": 200,
				  "startingStack": 10000,
				  "allowRebuys": true,
				  "allowAddOn": true,
				  "levels": []
				}
				""")).andExpect(status().isNoContent());

		verify(service).updateTournament(eq(TOURNAMENT_ID), any());
	}

}
