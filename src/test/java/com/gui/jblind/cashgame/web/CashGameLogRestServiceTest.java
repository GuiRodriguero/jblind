package com.gui.jblind.cashgame.web;

import com.gui.jblind.TestBase;
import com.gui.jblind.cashgame.CashGameLogService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashGameLogRestService.class)
class CashGameLogRestServiceTest extends TestBase {

	private static final String CASH_GAME_ID = randomUUID().toString();

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CashGameLogService service;

	@Override
	public void init() {
		// empty
	}

	@Test
	void should_log() throws Exception {
		when(service.createLog(any(), any())).thenReturn(valid(CashGameLogResponse.class));

		mockMvc
			.perform(post("/v1/cashgames/" + CASH_GAME_ID + "/logs").contentType(MediaType.APPLICATION_JSON).content("""
					{
					  "cashGamePlayerId": "5a22484e-5a76-486e-a5ee-9418c801771f",
					  "type": "BUY_IN",
					  "amount": 200.0,
					  "message": "Gui joined with $ 200.00"
					}
					"""))
			.andExpect(status().isCreated());

		InOrder inOrder = inOrder(service);
		inOrder.verify(service).createLog(any(), any());
		inOrder.verifyNoMoreInteractions();
	}

}
