package com.gui.jblind.tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
public class TournamentLevel {

	@Id
	@Builder.Default
	private String id = randomUUID().toString();

	private Integer roundNumber;

	private Integer smallBlind;

	private Integer bigBlind;

	private Integer ante;

	private Integer durationInMinutes;

	private boolean isBreak;

	private boolean shouldColorUp;

}
