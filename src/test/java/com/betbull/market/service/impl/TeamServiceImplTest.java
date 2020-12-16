package com.betbull.market.service.impl;

import com.betbull.market.model.Team;
import com.betbull.market.repository.TeamRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * Tests are written using Mockito BDD.
 */
@ExtendWith(MockitoExtension.class)
@Tag("unit")
class TeamServiceImplTest {

    @Mock
    TeamRepository repository;

    @InjectMocks
    TeamServiceImpl teamService;

    @Test
    void create() {
        Team team = new Team("Barcelona", "Spain", 5, BigDecimal.valueOf(200000.0));
        given(repository.save(team)).willReturn(team);

        Team saved = teamService.create(team);

        then(repository).should().save(team);
        assertEquals(team, saved);
    }

    @Test
    void updatePartOfEntity() {
        Team original = new Team("Barcelona", "Spain", 5, BigDecimal.valueOf(100000.0));
        original.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.of(original));
        given(repository.save(any(Team.class))).will(invocationOnMock -> invocationOnMock.getArgument(0));
        Team newTeamInfo = new Team("Dynamo", null, 7, null);
        newTeamInfo.setId(1L);

        Team updatedTeam = teamService.update(newTeamInfo);

        assertNotNull(updatedTeam);
        ArgumentCaptor<Team> teamCaptor = ArgumentCaptor.forClass(Team.class);
        then(repository).should().save(teamCaptor.capture());
        Team passedTeam = teamCaptor.getValue();
        assertAll("Check team details",
                () -> assertEquals(1L, passedTeam.getId()),
                () -> assertEquals("Dynamo", passedTeam.getTitle()),
                () -> assertEquals("Spain", passedTeam.getCountry()),
                () -> assertEquals(7, passedTeam.getCommissionPercent()),
                () -> assertEquals(0, passedTeam.getBalance().compareTo(BigDecimal.valueOf(100000.0))));

        assertEquals(passedTeam, updatedTeam);
    }

    @Test
    void updateFullEntity() {
        Team original = new Team("Barcelona", "Spain", 5, BigDecimal.valueOf(100000.0));
        original.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.of(original));
        given(repository.save(any(Team.class))).will(invocationOnMock -> invocationOnMock.getArgument(0));
        Team newTeamInfo = new Team("Dynamo", "Ukraine", 7, BigDecimal.valueOf(120000.0));
        newTeamInfo.setId(1L);

        Team updated = teamService.update(newTeamInfo);

        assertNotNull(updated);
        ArgumentCaptor<Team> captor = ArgumentCaptor.forClass(Team.class);
        then(repository).should().save(captor.capture());
        Team passedTeam = captor.getValue();
        assertEquals(newTeamInfo, passedTeam);
        assertEquals(passedTeam, updated);
    }

    @Test
    void deleteById() {
        given(repository.existsById(1L)).willReturn(true);
        teamService.deleteById(1L);
        then(repository).should().deleteById(1L);
    }

    @Test
    void deleteByIdNotExisting() {
        given(repository.existsById(1L)).willReturn(false);
        teamService.deleteById(1L);
        then(repository).should(never()).deleteById(1L);
    }

    @Test
    void getById() {
        Team team = new Team("Barcelona", "Spain", 5, BigDecimal.valueOf(100000.0));
        team.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.of(team));

        Optional<Team> found = teamService.getById(1L);

        then(repository).should().findById(1L);
        assertEquals(team, found.orElse(null));
    }

    @Test
    void getAll() {
        Team barcelona = new Team("Barcelona", "Spain", 5, BigDecimal.valueOf(100000.0));
        Team dynamo = new Team("Dynamo", "Ukraine", 3, BigDecimal.valueOf(120000.0));
        given(repository.findAll()).willReturn(Arrays.asList(barcelona, dynamo));

        List<Team> allTeams = teamService.getAll();

        then(repository).should().findAll();
        assertEquals(2, allTeams.size());
    }

    @Test
    void existsById() {
        given(repository.existsById(1L)).willReturn(true);

        assertTrue(teamService.existsById(1L));
        then(repository).should().existsById(1L);
        assertFalse(teamService.existsById(2L));
        then(repository).should().existsById(2L);
    }

    @Test
    void deleteAll() {
        teamService.deleteAll();

        then(repository).should().deleteAll();
        then(repository).shouldHaveNoMoreInteractions();
    }
}