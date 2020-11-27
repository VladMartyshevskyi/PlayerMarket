package com.betbull.market.service;

import com.betbull.market.model.Team;

import java.util.List;
import java.util.Optional;

/**
 * The interface TeamService declares methods for managing teams.
 */
public interface TeamService {

    /**
     * Create a new team.
     *
     * @param team the team
     * @return the team
     */
    Team create(Team team);

    /**
     * Update a team.
     *
     * @param team the team
     * @return the team
     */
    Team update(Team team);

    /**
     * Delete team by id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * Gets team by id.
     *
     * @param id the id
     * @return the by id
     */
    Optional<Team> getById(Long id);

    /**
     * Gets all teams.
     *
     * @return the all
     */
    List<Team> getAll();

    /**
     * Exists team by id.
     *
     * @param id the id
     * @return the boolean
     */
    boolean existsById(Long id);

    /**
     * Deletes all teams.
     */
    void deleteAll();
}
