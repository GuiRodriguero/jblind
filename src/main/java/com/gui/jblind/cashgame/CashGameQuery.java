package com.gui.jblind.cashgame;

import com.gui.jblind.core.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class CashGameQuery {

	private final CashGameRepository repository;

	CashGame findById(String id) {
		return repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cash Game not found with id: " + id));
	}

}
