package com.betbull.market.service;

import com.betbull.market.exception.IllegalTransferException;
import com.betbull.market.exception.NonSufficientFundsException;
import com.betbull.market.model.Contract;
import com.betbull.market.model.Player;
import com.betbull.market.model.Team;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractServiceTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ContractService contractService;

    @After
    public void cleanUp() {
        playerService.deleteAll();
        teamService.deleteAll();
    }

    @Test
    public void transferPlayerTest() throws IllegalTransferException {
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        Team wales = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        Team barcelona = teamService.create(new Team("Barcelona", "Spain", 10, 150000.0));
        // First contract
        Contract currentContract = contractService.transferPlayer(player, wales);
        assertTrue(currentContract.getActive());
        assertEquals(player.getId(), currentContract.getPlayer().getId());
        assertEquals(wales.getId(), currentContract.getTeam().getId());

        double walesBalanceBefore = wales.getBalance();
        double barcelonaBalanceBefore = barcelona.getBalance();

        // Transfer player to barcelona team
        Contract contract = contractService.transferPlayer(player, barcelona);
        Contract oldContract = contractService.getContractById(currentContract.getId()).orElse(null);
        assertNotNull(oldContract);
        assertFalse(oldContract.getActive());
        assertTrue(contract.getActive());
        assertEquals(player.getId(), contract.getPlayer().getId());
        assertEquals(barcelona.getId(), contract.getTeam().getId());


        // Check balances
        wales = teamService.getById(wales.getId()).orElse(null);
        barcelona = teamService.getById(barcelona.getId()).orElse(null);
        Double contractFee = contractService.calculateContractFee(player, wales);
        assertNotNull(wales);
        assertNotNull(barcelona);
        assertEquals(0, barcelona.getBalance().compareTo(barcelonaBalanceBefore - contractFee));
        assertEquals(0, wales.getBalance().compareTo(walesBalanceBefore + contractFee));
        assertEquals(contractFee, contract.getContractFee());
    }

    @Test
    public void transferPlayerIllegalTest() throws IllegalTransferException {
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        Team wales = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        // First contract
        contractService.transferPlayer(player, wales);
        assertThrows(IllegalTransferException.class, () -> contractService.transferPlayer(player, wales));
    }

    @Test
    public void transferPlayerNoFundsTest() throws IllegalTransferException {
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        Team wales = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        Team barcelona = teamService.create(new Team("Barcelona", "Spain", 10, 30000.0));
        // First contract
        contractService.transferPlayer(player, wales);
        assertThrows(NonSufficientFundsException.class, () -> contractService.transferPlayer(player, barcelona));
    }

    @Test
    public void calculateContractFeeTest() {
        Player player = new Player("John", "Doe", 22, 20);
        Team team = new Team("Wales", "UK", 10, 200000.0);
        Double calculatedFee = contractService.calculateContractFee(player, team);
        double transferFee = 1.0 * player.getExperience() * 100000 / player.getAge();
        double teamCommission = 1.0 * team.getCommissionPercent() / 100 * transferFee;
        double contractFee = transferFee + teamCommission;
        assertEquals(0, calculatedFee.compareTo(contractFee));
    }

    @Test
    public void getContractsByPlayerId() throws IllegalTransferException{
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        Team wales = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        Team barcelona = teamService.create(new Team("Barcelona", "Spain", 10, 150000.0));
        // First contract
        Contract currentContract = contractService.transferPlayer(player, wales);
        Contract newContract = contractService.transferPlayer(player, barcelona);
        List<Long> contractsIds = contractService.getContractsByPlayerId(player.getId()).stream().map(Contract::getId).collect(Collectors.toList());
        assertTrue(contractsIds.containsAll(Arrays.asList(currentContract.getId(), newContract.getId())));
    }

    @Test
    public void getTeamsByPlayerId() throws IllegalTransferException {
        Player player = playerService.create(new Player("John", "Doe", 22, 19));
        Team wales = teamService.create(new Team("Wales", "UK", 7, 200000.0));
        Team barcelona = teamService.create(new Team("Barcelona", "Spain", 10, 150000.0));
        // First contract
        Contract currentContract = contractService.transferPlayer(player, wales);
        Contract newContract = contractService.transferPlayer(player, barcelona);
        List<Long> teamIds = contractService.getTeamsByPlayerId(player.getId()).stream().map(Team::getId).collect(Collectors.toList());
        assertTrue(teamIds.containsAll(Arrays.asList(currentContract.getTeam().getId(), newContract.getTeam().getId())));
    }
}
