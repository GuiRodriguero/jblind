package com.gui.jblind.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface TournamentLogRepository extends JpaRepository<TournamentLog, Long> {

	List<TournamentLog> findAllByTournamentIdOrderByTimestampDesc(String tournamentId);

}
