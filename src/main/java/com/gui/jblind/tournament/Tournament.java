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
@Builder
@EqualsAndHashCode
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
	@JoinColumn(name = "tournament_id")
	@OneToMany(cascade = ALL, orphanRemoval = true)
	private List<TournamentLevel> levels = new ArrayList<>();

	public void addLevel(TournamentLevel level) {
		levels.add(level);
	}

	public Tournament startTournament() {
		this.status = IN_PROGRESS;
		return this;
	}

}
