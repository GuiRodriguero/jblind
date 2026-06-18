package com.gui.jblind.tournament.web;

import com.gui.jblind.tournament.TournamentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.SEE_OTHER;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/tournaments")
class TournamentRestService {

	private final TournamentService service;

	@GetMapping
	public ResponseEntity<List<TournamentSummaryResponse>> listTournaments() {
		return ResponseEntity.ok(service.listAllTournaments());
	}

	@PostMapping("/new")
	public ResponseEntity<Void> createTournament(@Valid @RequestBody TournamentRequest request) {
		String id = service.createTournament(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<TournamentDetailResponse> getById(@PathVariable String id) {
		return ResponseEntity.ok(service.getTournamentById(id));
	}

	@PostMapping("/{id}/play")
	public ResponseEntity<Void> playTournament(@PathVariable String id) {
		service.playTournament(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTournament(@PathVariable String id) {
		service.deleteTournament(id);
		return ResponseEntity.status(SEE_OTHER).header("Location", "http://localhost:5173/tournaments").build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateTournament(@PathVariable String id,
			@Valid @RequestBody TournamentRequest request) {
		service.updateTournament(id, request);
		return ResponseEntity.noContent().build();
	}

}
