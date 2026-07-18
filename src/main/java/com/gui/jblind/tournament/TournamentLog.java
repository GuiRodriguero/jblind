package com.gui.jblind.tournament;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
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
public class TournamentLog {

	@Id
	@Builder.Default
	private final String id = randomUUID().toString();

	@Column(nullable = false)
	private String tournamentId;

	@Column(nullable = false)
	private String playerId;

	@Enumerated(STRING)
	@Column(nullable = false)
	private TournamentLogType type;

	@Column(nullable = false)
	private BigDecimal amount;

	private String message;

	@Column(nullable = false)
	private LocalDateTime timestamp;

}
