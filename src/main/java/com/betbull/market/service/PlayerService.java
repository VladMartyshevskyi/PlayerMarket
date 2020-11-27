package com.betbull.market.service;

import com.betbull.market.model.Player;

import java.util.List;
import java.util.Optional;


/**
 * The interface PlayerService declares methods for managing players.
 */
public interface PlayerService {

    /**
     * Create player.
     *
     * @param player the player
     * @return the player
     */
    Player create(Player player);

    /**
     * Update player.
     *
     * @param player the player
     * @return the player
     */
    Player update(Player player);

    /**
     * Deletes player by id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * Gets player by id.
     *
     * @param id the id
     * @return the by id
     */
    Optional<Player> getById(Long id);

    /**
     * Gets all players.
     *
     * @return the all
     */
    List<Player> getAll();

    /**
     * Exists player by id.
     *
     * @param id the id
     * @return the boolean
     */
    boolean existsById(Long id);

    /**
     * Deletes all players.
     */
    void deleteAll();
}
