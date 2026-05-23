package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentRestService.class)
class TournamentRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TournamentService service;

	@Test
	void should_list_tournaments() throws Exception {
		TournamentSummaryResponse response = TestBase.valid(TournamentSummaryResponse.class);
		when(service.listAllTournaments()).thenReturn(List.of(response));

		mockMvc.perform(get("/v1/tournaments"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(response.id()))
			.andExpect(jsonPath("$[0].name").value(response.name()))
			.andExpect(jsonPath("$[0].scheduledAt").value(response.scheduledAt().toString()))
			.andExpect(jsonPath("$[0].expectedPlayers").value(response.expectedPlayers()))
			.andExpect(jsonPath("$[0].buyIn").value(response.buyIn()))
			.andExpect(jsonPath("$[0].status").value(response.status()));

		verify(service).listAllTournaments();
	}

	@Test
	void should_create_tournament() throws Exception {
		when(service.createTournament(any())).thenReturn(1L);

		mockMvc.perform(post("/v1/tournaments").contentType(MediaType.APPLICATION_JSON).content("""
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

		verify(service).createTournament(any());
	}

	@Test
	void should_get_tournament_by_id() throws Exception {
		TournamentDetailResponse response = TestBase.valid(TournamentDetailResponse.class);
		when(service.getTournamentById(1L)).thenReturn(response);

		mockMvc.perform(get("/v1/tournaments/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(response.id()))
			.andExpect(jsonPath("$.name").value(response.name()));

		verify(service).getTournamentById(1L);
	}

	@Test
	void should_play_tournament() throws Exception {
		mockMvc.perform(post("/v1/tournaments/1/play")).andExpect(status().isNoContent());

		verify(service).playTournament(1L);
	}

}
