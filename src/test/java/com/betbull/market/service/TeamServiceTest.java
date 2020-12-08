package com.betbull.market.service;

import com.betbull.market.model.Team;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @After
    public void cleanUp() {
        teamService.deleteAll();
    }

    @Test
    public void createTest() {
        Team team = new Team("Wales", "UK", 7, 200000.0);
        Team createdTeam = teamService.create(team);
        assertNotNull(createdTeam);
        assertEquals(team.getTitle(), createdTeam.getTitle());
        assertEquals(team.getCountry(), createdTeam.getCountry());
        assertEquals(team.getCommissionPercent(), createdTeam.getCommissionPercent());
        assertEquals(0, team.getBalance().compareTo(createdTeam.getBalance()));
        assertNotNull(createdTeam.getId());
    }

    @Test
    public void updateTest() {
       Team team = teamService.create(new Team("Wales", "UK", 7, 200000.0));
       team.setCountry("USA");
       team.setTitle("Texas");
       team.setBalance(150000.0);
       teamService.update(team);
       Team updated = teamService.getById(team.getId()).orElse(null);
       assertNotNull(updated);
       assertEquals(team.getCountry(), updated.getCountry());
       assertEquals(team.getTitle(), updated.getTitle());
       assertEquals(0, team.getBalance().compareTo(updated.getBalance()));
    }

    @Test
    public void deleteByIdTest() {
        Team team = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        teamService.deleteById(team.getId());
        assertEquals(Optional.empty(), teamService.getById(team.getId()));
    }

    @Test
    public void getByIdTest() {
        Team team = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        Team foundTeam = teamService.getById(team.getId()).orElse(null);
        assertNotNull(foundTeam);
        assertEquals(team.getId(), foundTeam.getId());
        Team notExistTeam = teamService.getById(10000L).orElse(null);
        assertNull(notExistTeam);
    }

    @Test
    public void getAllTest() {
        Team wales = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        Team barcelona = teamService.create(new Team("Barcelona", "Spain", 10, 150000.0));
        Team atalanta = teamService.create(new Team("Atalanta", "Italy", 9, 190000.0));
        List<Long> teamsIds = teamService.getAll().stream().map(Team::getId).collect(Collectors.toList());
        assertTrue(teamsIds.containsAll(Arrays.asList(wales.getId(), barcelona.getId(), atalanta.getId())));
    }

    @Test
    public void existsByIdTest() {
        Team team = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        assertTrue(teamService.existsById(team.getId()));
        assertFalse(teamService.existsById(10000L));
    }

    @Test
    public void deleteAllTest() {
        teamService.create(new Team("Wales", "UK", 7, 200000.0));
        teamService.create(new Team("Barcelona", "Spain", 10, 150000.0));
        teamService.deleteAll();
        assertEquals(Collections.emptyList(), teamService.getAll());
    }
}
