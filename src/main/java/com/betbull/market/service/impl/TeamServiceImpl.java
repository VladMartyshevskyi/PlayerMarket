package com.betbull.market.service.impl;

import com.betbull.market.model.Team;
import com.betbull.market.model.Team;
import com.betbull.market.repository.TeamRepository;
import com.betbull.market.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is used to manage football teams.
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    /**
     * Instantiates a new TeamService.
     *
     * @param teamRepository the team repository
     */
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team update(Team team) {
        Optional<Team> currentTeam = teamRepository.findById(team.getId());
        if (currentTeam.isPresent()) {
            Team current = currentTeam.get();
            if (team.getTitle() != null) {
                current.setTitle(team.getTitle());
            }
            if (team.getBalance() != null) {
                current.setBalance(team.getBalance());
            }
            if (team.getCommissionPercent() != null) {
                current.setCommissionPercent(team.getCommissionPercent());
            }
            if (team.getCountry() != null) {
                current.setCountry(team.getCountry());
            }
            return teamRepository.save(current);
        }
        return null;
    }

    @Override
    public Team create(Team team) {
        team.setId(null);
        return teamRepository.save(team);
    }

    @Override
    public void deleteById(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
        }
    }

    @Override
    public Optional<Team> getById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public List<Team> getAll() {
        return (List<Team>) teamRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        teamRepository.deleteAll();
    }
}
