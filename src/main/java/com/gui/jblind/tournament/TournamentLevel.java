package com.gui.jblind.tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;
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
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private Integer roundNumber;

	private Integer smallBlind;

	private Integer bigBlind;

	private Integer ante;

	private Integer durationInMinutes;

	private boolean isBreak;

}
