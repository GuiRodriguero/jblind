package com.gui.jblind.cashgame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashGameRepository extends JpaRepository<CashGame, String> {

}
