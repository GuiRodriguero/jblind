package com.gui.jblind.cashgame;

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
public class CashGamePlayer {

	@Id
	@Builder.Default
	private String id = randomUUID().toString();

	private String name;

	@Builder.Default
	private BigDecimal totalInvested = ZERO;

	@Builder.Default
	private BigDecimal currentStack = ZERO;

	public void addChips(BigDecimal amount) {
		totalInvested = totalInvested.add(amount);
		currentStack = currentStack.add(amount);
	}

	public void rebuy(BigDecimal amount) {
		totalInvested = totalInvested.add(amount);
		currentStack = amount;
	}

	public void cashout() {
		currentStack = ZERO;
	}

}
