package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGameService;
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

@WebMvcTest(CashGameRestService.class)
class CashGameRestServiceTest extends TestBase {

	private static final String CASH_GAME_ID = randomUUID().toString();

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CashGameService service;

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_list_cash_games() throws Exception {
		CashGameSummaryResponse response = Instancio.of(CashGameSummaryResponse.class)
			.set(field(CashGameSummaryResponse::id), CASH_GAME_ID)
			.create();
		when(service.listAllCashGames()).thenReturn(List.of(response));

		mockMvc.perform(get("/v1/cashgames"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(CASH_GAME_ID))
			.andExpect(jsonPath("$[0].name").value(response.name()))
			.andExpect(
					jsonPath("$[0].scheduledAt").value(startsWith(response.scheduledAt().toString().substring(0, 19))))
			.andExpect(jsonPath("$[0].smallBlind").value(response.smallBlind().doubleValue()))
			.andExpect(jsonPath("$[0].bigBlind").value(response.bigBlind().doubleValue()))
			.andExpect(jsonPath("$[0].minBuyIn").value(response.minBuyIn().doubleValue()))
			.andExpect(jsonPath("$[0].maxBuyIn").value(response.maxBuyIn().doubleValue()))
			.andExpect(jsonPath("$[0].status").value(response.status()));

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).listAllCashGames();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_create_cash_game() throws Exception {
		when(service.createCashGame(any())).thenReturn(CASH_GAME_ID);

		mockMvc.perform(post("/v1/cashgames/new").contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "name": "Cash Game Name",
				  "scheduledAt": "2023-12-01T10:00:00",
				  "minBuyIn": 100,
				  "maxBuyIn": 500,
				  "smallBlind": 1,
				  "bigBlind": 2
				}
				""")).andExpect(status().isCreated());

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).createCashGame(any());
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_get_cash_game_by_id() throws Exception {
		CashGameDetailResponse response = Instancio.of(CashGameDetailResponse.class)
			.set(field(CashGameDetailResponse::id), CASH_GAME_ID)
			.create();
		when(service.getCashGameById(CASH_GAME_ID)).thenReturn(response);

		mockMvc.perform(get("/v1/cashgames/" + CASH_GAME_ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(CASH_GAME_ID))
			.andExpect(jsonPath("$.name").value(response.name()))
			.andExpect(jsonPath("$.smallBlind").value(response.smallBlind().doubleValue()))
			.andExpect(jsonPath("$.bigBlind").value(response.bigBlind().doubleValue()))
			.andExpect(jsonPath("$.minBuyIn").value(response.minBuyIn().doubleValue()))
			.andExpect(jsonPath("$.maxBuyIn").value(response.maxBuyIn().doubleValue()))
			.andExpect(jsonPath("$.status").value(response.status()));

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).getCashGameById(CASH_GAME_ID);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_play_cash_game() throws Exception {
		mockMvc.perform(post("/v1/cashgames/" + CASH_GAME_ID + "/play")).andExpect(status().isNoContent());

		verify(service).playCashGame(CASH_GAME_ID);
	}

	@Test
	void should_finish_cash_game() throws Exception {
		mockMvc.perform(post("/v1/cashgames/" + CASH_GAME_ID + "/finish")).andExpect(status().isNoContent());

		verify(service).finishCashGame(CASH_GAME_ID);
	}

	@Test
	void should_delete_cash_game() throws Exception {
		mockMvc.perform(delete("/v1/cashgames/" + CASH_GAME_ID)).andExpect(status().isSeeOther());

		verify(service).deleteCashGame(CASH_GAME_ID);
	}

	@Test
	void should_update_cash_game() throws Exception {
		mockMvc.perform(put("/v1/cashgames/" + CASH_GAME_ID).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "name": "Updated Cash Game",
				  "scheduledAt": "2023-12-01T10:00:00",
				  "minBuyIn": 200,
				  "maxBuyIn": 1000,
				  "smallBlind": 2,
				  "bigBlind": 4
				}
				""")).andExpect(status().isNoContent());

		verify(service).updateCashGame(eq(CASH_GAME_ID), any());
	}

	@Test
	void should_add_player() throws Exception {
		CashGamePlayerResponse response = Instancio.create(CashGamePlayerResponse.class);
		when(service.addPlayer(eq(CASH_GAME_ID), any())).thenReturn(response);

		mockMvc
			.perform(post("/v1/cashgames/" + CASH_GAME_ID + "/players").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "Player Name"
						}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(response.id()))
			.andExpect(jsonPath("$.name").value(response.name()))
			.andExpect(jsonPath("$.totalInvested").value(response.totalInvested().doubleValue()))
			.andExpect(jsonPath("$.currentStack").value(response.currentStack().doubleValue()));

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).addPlayer(eq(CASH_GAME_ID), any());
		inOrder.verifyNoMoreInteractions();
	}

}
