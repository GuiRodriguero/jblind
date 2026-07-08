package com.gui.jblind.cashgame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashGamePlayerRepository extends JpaRepository<CashGamePlayer, String> {

}
