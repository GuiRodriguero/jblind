package com.gui.jblind.tournament;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Table
@Entity
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(staticName = "of", access = PACKAGE)
public class TournamentPrize {

	@Id
	@Builder.Default
	private String id = randomUUID().toString();

	@Enumerated(STRING)
	private PrizeMode mode;

	private BigDecimal total;

	@Builder.Default
	@OrderBy("position ASC")
	@JoinColumn(name = "prize_id")
	@OneToMany(cascade = ALL, orphanRemoval = true)
	private List<TournamentPrizePayout> payouts = new ArrayList<>();

	public void addPayout(TournamentPrizePayout payout) {
		payouts.add(payout);
	}

}
