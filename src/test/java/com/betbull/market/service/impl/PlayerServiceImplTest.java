package com.betbull.market.service.impl;

import com.betbull.market.model.Player;
import com.betbull.market.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    PlayerRepository repository;

    @InjectMocks
    PlayerServiceImpl playerService;

    @Test
    void create() {
        Player player = new Player("John", "Doe", 22, 14);
        when(repository.save(player)).thenReturn(player);

        Player saved = playerService.create(player);

        verify(repository).save(player);
        assertEquals(player, saved);
    }

    @Test
    void updatePartOfEntity() {
        Player original = new Player("John", "Doe", 22, 14);
        original.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(original));
        when(repository.save(any(Player.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        Player newPlayerInfo = new Player(null, "Smith", 23, null);
        newPlayerInfo.setId(1L);
        Player updatedPlayer = playerService.update(newPlayerInfo);

        assertNotNull(updatedPlayer);
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(repository).save(captor.capture());
        Player passedPlayer = captor.getValue();
        assertAll("Check player details",
                () -> assertEquals(1L, passedPlayer.getId()),
                () -> assertEquals("John", passedPlayer.getFirstName()),
                () -> assertEquals("Smith", passedPlayer.getLastName()),
                () -> assertEquals(23, passedPlayer.getAge()),
                () -> assertEquals(14, passedPlayer.getExperience()));

        assertEquals(passedPlayer, updatedPlayer);
    }

    @Test
    void updateFullEntity() {
        Player original = new Player("John", "Doe", 22, 14);
        original.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(original));
        when(repository.save(any(Player.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Player newPlayerInfo = new Player("Bob", "Smith", 21, 16);
        newPlayerInfo.setId(1L);
        Player updated = playerService.update(newPlayerInfo);

        assertNotNull(updated);
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(repository).save(captor.capture());
        Player passedPlayer = captor.getValue();
        assertEquals(newPlayerInfo, passedPlayer);
        assertEquals(passedPlayer, updated);
    }

    @Test
    void deleteById() {
        when(repository.existsById(1L)).thenReturn(true);
        playerService.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteByIdNotExisting() {
        when(repository.existsById(1L)).thenReturn(false);
        playerService.deleteById(1L);
        verify(repository, never()).deleteById(1L);
    }

    @Test
    void getById() {
        Player player = new Player("John", "Doe", 22, 14);
        player.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(player));

        Optional<Player> found = playerService.getById(1L);

        verify(repository).findById(1L);
        assertEquals(player, found.orElse(null));
    }

    @Test
    void getAll() {
        Player tom = new Player("Tom", "Doe", 22, 14);
        Player john = new Player("John", "Smith", 22, 14);
        when(repository.findAll()).thenReturn(Arrays.asList(tom, john));

        List<Player> allPlayers = playerService.getAll();

        verify(repository).findAll();
        assertEquals(2, allPlayers.size());
    }

    @Test
    void existsById() {
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(playerService.existsById(1L));
        verify(repository).existsById(1L);
        assertFalse(playerService.existsById(2L));
        verify(repository).existsById(2L);
    }

    @Test
    void deleteAll() {
        playerService.deleteAll();
        verify(repository).deleteAll();
        verifyNoMoreInteractions(repository);
    }
}