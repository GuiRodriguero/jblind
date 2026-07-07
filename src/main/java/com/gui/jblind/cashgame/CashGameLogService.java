package com.gui.jblind.cashgame;

import com.gui.jblind.cashgame.web.CashGameLogRequest;
import com.gui.jblind.cashgame.web.CashGameLogResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CashGameLogService {

	private final CashGameLogRepository repository;

	public CashGameLogResponse createLog(String cashGameId, CashGameLogRequest request) {
		CashGameLog log = CashGameLog.builder()
			.cashGameId(cashGameId)
			.type(request.type())
			.amount(request.amount())
			.message(request.message())
			.timestamp(LocalDateTime.now())
			.build();

		return CashGameLogResponse.from(repository.save(log));
	}

}
