package com.gui.jblind.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, String> {

}
