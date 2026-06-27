package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGameService;
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
@RequestMapping("/v1/cashgames")
class CashGameRestService {

	private final CashGameService service;

	@GetMapping
	public ResponseEntity<List<CashGameSummaryResponse>> listCashGames() {
		return ResponseEntity.ok(service.listAllCashGames());
	}

	@PostMapping("/new")
	public ResponseEntity<Void> createCashGame(@Valid @RequestBody CashGameRequest request) {
		String id = service.createCashGame(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CashGameDetailResponse> getById(@PathVariable String id) {
		return ResponseEntity.ok(service.getCashGameById(id));
	}

	@PostMapping("/{id}/play")
	public ResponseEntity<Void> playCashGame(@PathVariable String id) {
		service.playCashGame(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCashGame(@PathVariable String id) {
		service.deleteCashGame(id);
		return ResponseEntity.status(SEE_OTHER).header("Location", "http://localhost:5173/cash-games").build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateCashGame(@PathVariable String id, @Valid @RequestBody CashGameRequest request) {
		service.updateCashGame(id, request);
		return ResponseEntity.noContent().build();
	}

}
