package com.betbull.market.service.impl;

import com.betbull.market.exception.IllegalTransferException;
import com.betbull.market.exception.NonSufficientFundsException;
import com.betbull.market.model.Contract;
import com.betbull.market.model.Player;
import com.betbull.market.model.Team;
import com.betbull.market.repository.ContractRepository;
import com.betbull.market.service.ContractService;
import com.betbull.market.service.TeamService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Contract service used to manage contracts between football players and teams.
 */
@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final TeamService teamService;

    /**
     * Instantiates a new Contract service.
     *
     * @param contractRepository the contract repository
     * @param teamService        the team service
     */
    public ContractServiceImpl(ContractRepository contractRepository, TeamService teamService) {
        this.contractRepository = contractRepository;
        this.teamService = teamService;
    }

    @Override
    @Transactional
    public Contract transferPlayer(Player player, Team team) throws IllegalTransferException {
        Contract currentContract = contractRepository.findByPlayerIdAndActiveTrue(player.getId());
        double contractFee = 0.0;
        if (currentContract != null) {
            Team currentTeam = currentContract.getTeam();
            if (currentTeam.getId().equals(team.getId())) {
                throw new IllegalTransferException("Player has active contract with the specified team");
            }
            contractFee = calculateContractFee(player, currentTeam);
            transferFunds(team, currentTeam, contractFee);
            currentContract.setActive(false);
            contractRepository.save(currentContract);
        }
        Contract contract = new Contract(player, team, contractFee);
        contractRepository.save(contract);
        return contract;
    }

    @Override
    public double calculateContractFee(Player player, Team team) {
        double transferFee = 1.0 * player.getExperience() * 100000 / player.getAge();
        double teamCommission = 1.0 * team.getCommissionPercent() / 100 * transferFee;
        return transferFee + teamCommission;
    }

    @Override
    public List<Contract> getContractsByPlayerId(Long playerId) {
        return contractRepository.findByPlayerId(playerId);
    }

    @Override
    public List<Team> getTeamsByPlayerId(Long id) {
        return getContractsByPlayerId(id).stream().map(Contract::getTeam).collect(Collectors.toList());
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    /**
     * Transfer funds between teams.
     *
     * @param teamFrom the team from
     * @param teamTo   the team to
     * @param amount   the amount
     * @throws NonSufficientFundsException the non sufficient funds exception
     */
    @Transactional
    protected void transferFunds(Team teamFrom, Team teamTo, double amount) throws NonSufficientFundsException {
        if (teamFrom.getBalance().compareTo(amount) >= 0) {
            teamFrom.setBalance(teamFrom.getBalance() - amount);
            teamTo.setBalance(teamTo.getBalance() + amount);
            teamService.update(teamFrom);
            teamService.update(teamTo);
        } else {
            throw new NonSufficientFundsException("Team " + teamFrom.getTitle() + " doesn't have " + amount + " funds on balance");
        }
    }
}
