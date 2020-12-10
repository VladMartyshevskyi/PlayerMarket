package com.betbull.market.service.impl;

import com.betbull.market.model.Team;
import com.betbull.market.repository.TeamRepository;
import com.betbull.market.infra.ProcessedBean;
import com.betbull.market.service.PlayerService;
import com.betbull.market.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * This class is used to manage football teams.
 */
@Service
@ProcessedBean
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    static {
        System.out.println("TeamServiceImpl static initialization");
    }

    /**
     * Instantiates a new TeamService.
     *
     * @param teamRepository the team repository
     */
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
        System.out.println("TeamServiceImpl constructor");
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        System.out.println("TeamServiceImpl setter injection of playerService");
    }

    @Override
    public Team update(Team team) {
        return teamRepository.findById(team.getId()).map(current -> {
            Optional.ofNullable(team.getTitle()).ifPresent(current::setTitle);
            Optional.ofNullable(team.getBalance()).ifPresent(current::setBalance);
            Optional.ofNullable(team.getCommissionPercent()).ifPresent(current::setCommissionPercent);
            Optional.ofNullable(team.getCountry()).ifPresent(current::setCountry);
            return current;
        }).map(teamRepository::save).orElse(null);
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

    @PostConstruct
    public void init() {
        System.out.println("TeamServiceImpl POST-Construct");
    }
}
