package com.betbull.market.service.impl;

import com.betbull.market.exception.IllegalTransferException;
import com.betbull.market.exception.NonSufficientFundsException;
import com.betbull.market.model.Contract;
import com.betbull.market.model.Player;
import com.betbull.market.model.Team;
import com.betbull.market.repository.ContractRepository;
import com.betbull.market.service.ContractService;
import com.betbull.market.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Contract service used to manage contracts between football players and teams.
 */
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private static final BigDecimal CONTRACT_FEE_MULTIPLIER = BigDecimal.valueOf(100000);
    private static final BigDecimal ONE_HUNDRED_PERCENTS = BigDecimal.valueOf(100);

    private final ContractRepository contractRepository;
    private final TeamService teamService;

    @Override
    @Transactional
    public Contract transferPlayer(Player player, Team team) throws IllegalTransferException {
        Contract currentContract = contractRepository.findByPlayerIdAndActiveTrue(player.getId());
        BigDecimal contractFee = BigDecimal.ZERO;
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
    public BigDecimal calculateContractFee(Player player, Team team) {
        BigDecimal transferFee =
                BigDecimal.valueOf(player.getExperience())
                .multiply(CONTRACT_FEE_MULTIPLIER)
                .divide(BigDecimal.valueOf(player.getAge()), 2, RoundingMode.CEILING);
        BigDecimal teamCommission = BigDecimal.valueOf(team.getCommissionPercent()).divide(ONE_HUNDRED_PERCENTS, 2, RoundingMode.CEILING).multiply(transferFee);
        return transferFee.add(teamCommission).setScale(2, RoundingMode.CEILING);
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
    protected void transferFunds(Team teamFrom, Team teamTo, BigDecimal amount) throws NonSufficientFundsException {
        if (teamFrom.getBalance().compareTo(amount) >= 0) {
            teamFrom.setBalance(teamFrom.getBalance().subtract(amount));
            teamTo.setBalance(teamTo.getBalance().add(amount));
            teamService.update(teamFrom);
            teamService.update(teamTo);
        } else {
            throw new NonSufficientFundsException("Team " + teamFrom.getTitle() + " doesn't have " + amount + " funds on balance");
        }
    }
}
