package com.betbull.market.controller;

import com.betbull.market.controller.dto.TransferDTO;
import com.betbull.market.exception.IllegalTransferException;
import com.betbull.market.model.Contract;
import com.betbull.market.model.Player;
import com.betbull.market.model.Team;
import com.betbull.market.service.ContractService;
import com.betbull.market.service.PlayerService;
import com.betbull.market.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * The PlayerController is a REST API controller for managing football players.
 */
@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    private final ContractService contractService;
    private final TeamService teamService;

    /**
     * Gets all players.
     *
     * @return the all
     */
    @GetMapping("/players")
    public List<Player> getAll() {
        return playerService.getAll();
    }

    /**
     * Get player by id.
     *
     * @param id the id
     * @return the player
     */
    @GetMapping("/players/{id}")
    public Player get(@PathVariable(name = "id") Long id)  {
       Optional<Player> player = playerService.getById(id);
       if (player.isPresent()) {
           return player.get();
       } else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such player");
       }
    }

    /**
     * Gets player's contracts.
     *
     * @param id the id
     * @return the contracts
     */
    @GetMapping("/players/{id}/contracts")
    public List<Contract> getContracts(@PathVariable(name = "id") Long id)  {
        return contractService.getContractsByPlayerId(id);
    }

    /**
     * Gets player's teams.
     *
     * @param id the id
     * @return the teams
     */
    @GetMapping("/players/{id}/teams")
    public List<Team> getTeams(@PathVariable(name = "id") Long id)  {
        return contractService.getTeamsByPlayerId(id);
    }

    /**
     * Create a new player.
     *
     * @param player the player
     * @return the player
     */
    @PostMapping("/players")

    public Player create(@Valid @RequestBody Player player) {
        player.setId(null);
        return playerService.create(player);
    }

    /**
     * Transfer the player from one team to another.
     *
     * @param id          the player id
     * @param transferDTO the transfer dto
     * @return the signed contract
     */
    @PostMapping("/players/{id}/transfer")
    public Contract transfer(@PathVariable("id") Long id, @Valid @RequestBody TransferDTO transferDTO) {
        if (!id.equals(transferDTO.getPlayerId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player id is different from one provided in request body");
        }
        Optional<Player> player = playerService.getById(transferDTO.getPlayerId());
        Optional<Team> team = teamService.getById(transferDTO.getTeamId());
        if (!player.isPresent() || !team.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such player or team");
        }
        try {
            return contractService.transferPlayer(player.get(), team.get());
        } catch (IllegalTransferException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Update player.
     *
     * @param id     the player id
     * @param player the player
     * @return the player
     */
    @PutMapping("/players/{id}")
    public Player update(@PathVariable("id") Long id, @RequestBody Player player) {
        player.setId(id);
        if (playerService.existsById(id)) {
            return playerService.update(player);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such player");
     }

    /**
     * Delete player.
     *
     * @param id the id
     */
    @DeleteMapping("/players/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        playerService.deleteById(id);
    }

}
