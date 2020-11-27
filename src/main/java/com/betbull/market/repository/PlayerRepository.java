package com.betbull.market.repository;

import com.betbull.market.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface PlayerRepository declares methods for managing players in the database.
 */
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
}
