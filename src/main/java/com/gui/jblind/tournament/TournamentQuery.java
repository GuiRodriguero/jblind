package com.gui.jblind.tournament;

import com.gui.jblind.core.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
class TournamentQuery {

	private final TournamentRepository repository;

	Tournament findById(String id) {
		return repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));
	}

}
