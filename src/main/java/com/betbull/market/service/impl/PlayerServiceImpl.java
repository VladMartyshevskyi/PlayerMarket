package com.betbull.market.service.impl;

import com.betbull.market.model.Player;
import com.betbull.market.repository.PlayerRepository;
import com.betbull.market.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is used to manage football players.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Instantiates a new PlayerService.
     *
     * @param playerRepository the player repository
     */
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player update(Player player) {
        return playerRepository.findById(player.getId()).map(current -> {
            if (player.getFirstName() != null) {
                current.setFirstName(player.getFirstName());
            }
            if (player.getLastName() != null) {
                current.setLastName(player.getLastName());
            }
            if (player.getAge() != null) {
                current.setAge(player.getAge());
            }
            if (player.getExperience() != null) {
                current.setExperience(player.getExperience());
            }
            return current;
        }).map(playerRepository::save).orElse(null);
    }

    @Override
    public Player create(Player player) {
        player.setId(null);
        return playerRepository.save(player);
    }

    @Override
    public void deleteById(Long id)  {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
        }
    }
    @Override
    public Optional<Player> getById(Long id) {
        return playerRepository.findById(id);
    }
    @Override
    public List<Player> getAll() {
        return (List<Player>) playerRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        playerRepository.deleteAll();
    }
}
