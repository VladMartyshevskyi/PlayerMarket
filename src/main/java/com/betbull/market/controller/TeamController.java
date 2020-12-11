package com.betbull.market.controller;

import com.betbull.market.model.Team;
import com.betbull.market.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * The TeamController is a REST API controller for managing football teams.
 */
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * Gets all teams.
     *
     * @return the all
     */
    @GetMapping("/teams")
    public List<Team> getAll() {
        return teamService.getAll();
    }


    /**
     * Get team by id.
     *
     * @param id the id
     * @return the team
     */
    @GetMapping("/teams/{id}")
    public Team get(@PathVariable(name = "id") Long id) {
        Optional<Team> team = teamService.getById(id);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such team");
        }
    }

    /**
     * Create a new team.
     *
     * @param team the team
     * @return the team
     */
    @PostMapping("/teams")
    public Team create(@Valid @RequestBody Team team) {
        team.setId(null);
        return teamService.create(team);
    }

    /**
     * Update the team.
     *
     * @param id   the team id
     * @param team the team
     * @return the team
     */
    @PutMapping("/teams/{id}")
    public Team update(@PathVariable("id") Long id, @RequestBody Team team)  {
        team.setId(id);
        if (teamService.existsById(id)) {
            return teamService.update(team);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such team");
    }

    /**
     * Delete the team.
     *
     * @param id the id
     */
    @DeleteMapping("/teams/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        teamService.deleteById(id);
    }

}
