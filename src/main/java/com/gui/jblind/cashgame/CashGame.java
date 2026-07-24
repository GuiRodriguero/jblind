package com.gui.jblind.cashgame;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gui.jblind.cashgame.CashGameStatus.FINISHED;
import static com.gui.jblind.cashgame.CashGameStatus.IN_PROGRESS;
import static jakarta.persistence.CascadeType.ALL;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Table
@Entity
@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(staticName = "of", access = PACKAGE)
public class CashGame {

	@Id
	@Builder.Default
	private final String id = randomUUID().toString();

	private String name;

	private LocalDateTime scheduledAt;

	private BigDecimal minBuyIn;

	private BigDecimal maxBuyIn;

	private BigDecimal smallBlind;

	private BigDecimal bigBlind;

	@Enumerated(EnumType.STRING)
	private CashGameStatus status;

	@Builder.Default
	@JoinColumn(name = "cashgame_id")
	@OneToMany(cascade = ALL, orphanRemoval = true)
	private List<CashGamePlayer> players = new ArrayList<>();

	public void addPlayer(CashGamePlayer player) {
		players.add(player);
	}

	public CashGame start() {
		this.status = IN_PROGRESS;
		return this;
	}

	public CashGame finish() {
		this.status = FINISHED;
		return this;
	}

}
