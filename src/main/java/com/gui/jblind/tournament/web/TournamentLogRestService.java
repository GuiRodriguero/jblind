package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentLogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/tournaments/{id}/logs")
class TournamentLogRestService {

	private final TournamentLogService service;

	@PostMapping
	@ResponseStatus(CREATED)
	public TournamentLogResponse log(@PathVariable String id, @Valid @RequestBody TournamentLogRequest request) {
		return service.createLog(id, request);
	}

}
