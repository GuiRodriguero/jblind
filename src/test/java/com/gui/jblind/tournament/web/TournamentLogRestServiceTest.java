package com.gui.jblind.tournament.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.tournament.TournamentLogService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentLogRestService.class)
class TournamentLogRestServiceTest extends TestBase {

	private static final String TOURNAMENT_ID = randomUUID().toString();

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TournamentLogService service;

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_log() throws Exception {
		when(service.createLog(any(), any())).thenReturn(valid(TournamentLogResponse.class));

		mockMvc.perform(post("/v1/tournaments/" + TOURNAMENT_ID + "/logs").contentType(APPLICATION_JSON).content("""
				{
				  "tournamentPlayerId": "5a22484e-5a76-486e-a5ee-9418c801771f",
				  "type": "BUY_IN",
				  "amount": 200.0,
				  "message": "Gui joined with $ 200.00",
				  "playersLeft": 10
				}
				""")).andExpect(status().isCreated());

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).createLog(any(), any());
		inOrder.verifyNoMoreInteractions();
	}

}
