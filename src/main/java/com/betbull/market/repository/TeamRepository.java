package com.betbull.market.repository;

import com.betbull.market.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface TeamRepository declares methods for managing teams in the database.
 */
@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
}
