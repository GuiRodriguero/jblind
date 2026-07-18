package com.gui.jblind.tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
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
public class TournamentPlayer {

	@Id
	@Builder.Default
	private String id = randomUUID().toString();

	private String name;

	@Builder.Default
	private Integer entries = 0;

	@Builder.Default
	private Integer eliminationsMade = 0;

	@Builder.Default
	private BigDecimal totalInvested = ZERO;

	private Boolean addOn;

	private Integer finalPosition;

	public void addEntry(BigDecimal amount) {
		this.entries = (entries == null ? 0 : entries) + 1;
		this.totalInvested = (totalInvested == null ? ZERO : totalInvested).add(amount);
	}

	public void addOn(BigDecimal amount) {
		this.addOn = true;
		this.totalInvested = (totalInvested == null ? ZERO : totalInvested).add(amount);
	}

	public void finalPosition(Integer finalPosition) {
		this.finalPosition = finalPosition;
	}

	public void eliminate() {
		this.eliminationsMade = (eliminationsMade == null ? 0 : eliminationsMade) + 1;
	}

}
