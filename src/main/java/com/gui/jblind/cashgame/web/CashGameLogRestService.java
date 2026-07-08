package com.gui.jblind.cashgame.web;

import com.gui.jblind.cashgame.CashGameLogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/cashgames/{id}/logs")
class CashGameLogRestService {

	private final CashGameLogService service;

	@PostMapping
	@ResponseStatus(CREATED)
	public CashGameLogResponse log(@PathVariable String id, @Valid @RequestBody CashGameLogRequest request) {
		return service.createLog(id, request);
	}

}
