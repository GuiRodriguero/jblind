package com.gui.jblind.tournament;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gui.jblind.tournament.TournamentStatus.IN_PROGRESS;
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
public class Tournament {

	@Id
	@Builder.Default
	private final String id = randomUUID().toString();

	private String name;

	private LocalDateTime scheduledAt;

	private Integer expectedPlayers;

	private BigDecimal buyIn;

	private Integer startingStack;

	private boolean allowRebuys;

	private boolean allowAddOn;

	@Enumerated(EnumType.STRING)
	private TournamentStatus status;

	@Builder.Default
	@OrderBy("roundNumber ASC")
	@JoinColumn(name = "tournament_id")
	@OneToMany(cascade = ALL, orphanRemoval = true)
	private List<TournamentLevel> levels = new ArrayList<>();

	@Builder.Default
	@JoinColumn(name = "tournament_id")
	@OneToMany(cascade = ALL, orphanRemoval = true)
	private List<TournamentPlayer> players = new ArrayList<>();

	@OneToOne(cascade = ALL, orphanRemoval = true)
	@JoinColumn(name = "prize_id")
	private TournamentPrize prize;

	public void addLevel(TournamentLevel level) {
		levels.add(level);
	}

	public void addPlayer(TournamentPlayer player) {
		players.add(player);
	}

	public Tournament startTournament() {
		this.status = IN_PROGRESS;
		return this;
	}

}
