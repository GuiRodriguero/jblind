package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentPrize;
import org.junit.jupiter.api.Test;

import static com.gui.jblind.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentPrizeResponseTest {

	private final TournamentPrize entity = valid(TournamentPrize.class);

	@Test
	void should_convert_to_entity() {
		assertThat(TournamentPrizeResponse.of(entity)).isEqualTo(expected(entity));
	}

	@Test
	void should_return_null_when_entity_is_null() {
		assertThat(TournamentPrizeResponse.of(null)).isNull();
	}

	private TournamentPrizeResponse expected(TournamentPrize entity) {
		return new TournamentPrizeResponse(entity.getMode(),
				entity.getPayouts().stream().map(TournamentPrizePayoutResponse::of).toList());
	}

}
