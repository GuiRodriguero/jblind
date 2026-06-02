package com.gui.jblind.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

interface TournamentRepository extends JpaRepository<Tournament, String> {

}