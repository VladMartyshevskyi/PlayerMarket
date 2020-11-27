package com.betbull.market.service;

import com.betbull.market.exception.IllegalTransferException;
import com.betbull.market.model.Contract;
import com.betbull.market.model.Player;
import com.betbull.market.model.Team;

import java.util.List;
import java.util.Optional;

/**
 * The interface ContractService declares methods for managing contracts.
 */
public interface ContractService {

    /**
     * Transfer player contract.
     *
     * @param player the player
     * @param team   the team
     * @return the contract
     * @throws IllegalTransferException the illegal transfer exception
     */
    Contract transferPlayer(Player player, Team team) throws IllegalTransferException;

    /**
     * Calculate contract fee double.
     *
     * @param player the player
     * @param team   the team
     * @return the double
     */
    double calculateContractFee(Player player, Team team);

    /**
     * Gets contracts by player id.
     *
     * @param id the id
     * @return the contracts by player id
     */
    List<Contract> getContractsByPlayerId(Long id);

    /**
     * Gets teams by player id.
     *
     * @param id the id
     * @return the teams by player id
     */
    List<Team> getTeamsByPlayerId(Long id);

    /**
     * Gets contract by id.
     *
     * @param id the id
     * @return the contract
     */
    Optional<Contract> getContractById(Long id);
}
