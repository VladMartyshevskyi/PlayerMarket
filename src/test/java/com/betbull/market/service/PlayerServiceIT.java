package com.betbull.market.service;

import com.betbull.market.model.Player;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
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
public class PlayerServiceIT {
    @Autowired
    private PlayerService playerService;

    @After
    public void cleanUp() {
        playerService.deleteAll();
    }

    @Test
    public void createTest() {
        Player player = new Player("John", "Doe", 22, 19);
        Player createdPlayer = playerService.create(player);
        assertNotNull(createdPlayer);
        assertEquals(player.getFirstName(), createdPlayer.getFirstName());
        assertEquals(player.getLastName(), createdPlayer.getLastName());
        assertEquals(player.getAge(), createdPlayer.getAge());
        assertEquals(player.getExperience(), createdPlayer.getExperience());
        assertNotNull(createdPlayer.getId());
    }

    @Test
    public void updateTest() {
       Player player = playerService.create(new Player("John", "Doe", 22, 19));
       player.setExperience(24);
       player.setFirstName("James");
       player.setLastName("Smith");
       playerService.update(player);
       Player updated = playerService.getById(player.getId()).orElse(null);
       assertNotNull(updated);
       assertEquals(player.getExperience(), updated.getExperience());
       assertEquals(player.getFirstName(), updated.getFirstName());
       assertEquals(player.getLastName(), updated.getLastName());
    }

    @Test
    public void deleteByIdTest() {
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        playerService.deleteById(player.getId());
        assertEquals(Optional.empty(), playerService.getById(player.getId()));
    }

    @Test
    public void getByIdTest() {
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        Player foundPlayer = playerService.getById(player.getId()).orElse(null);
        assertNotNull(foundPlayer);
        assertEquals(player.getId(), foundPlayer.getId());
        Player notExistPlayer = playerService.getById(10000L).orElse(null);
        assertNull(notExistPlayer);
    }

    @Test
    public void getAllTest() {
        Player player1 = playerService.create(new Player("John", "Doe", 22, 19));
        Player player2 = playerService.create(new Player("Diego", "Smith", 23, 22));
        Player player3 = playerService.create(new Player("Peter", "Safi", 21, 25));
        List<Long> playersIds = playerService.getAll().stream().map(Player::getId).collect(Collectors.toList());
        assertTrue(playersIds.containsAll(Arrays.asList(player1.getId(), player2.getId(), player3.getId())));
    }

    @Test
    public void existsByIdTest() {
        Player john = playerService.create(new Player("John", "Doe", 22, 19));
        assertTrue(playerService.existsById(john.getId()));
        assertFalse(playerService.existsById(10000L));
    }

    @Test
    public void deleteAllTest() {
        playerService.create(new Player("John", "Doe", 22, 19));
        playerService.create(new Player("Diego", "Smith", 23, 22));
        playerService.deleteAll();
        assertEquals(Collections.emptyList(), playerService.getAll());
    }
}
