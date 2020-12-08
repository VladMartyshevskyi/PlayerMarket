package com.betbull.market.service.impl;

import com.betbull.market.exception.IllegalTransferException;
import com.betbull.market.exception.NonSufficientFundsException;
import com.betbull.market.model.Contract;
import com.betbull.market.model.Player;
import com.betbull.market.model.Team;
import com.betbull.market.repository.ContractRepository;
import com.betbull.market.service.TeamService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ContractServiceImplTest {

    @Mock
    TeamService teamService;

    @Mock
    ContractRepository contractRepository;

    @InjectMocks
    ContractServiceImpl contractService;

    @Test
    void transferPlayerWithoutContract() throws IllegalTransferException {
        Player player = new Player("John", "Doe", 24, 18);
        player.setId(1L);
        Team team = new Team("Barcelona", "Spain", 5, 100000.0);

        Contract contract = contractService.transferPlayer(player, team);

        assertEquals(0.0, contract.getContractFee());
        verify(contractRepository, times(1)).findByPlayerIdAndActiveTrue(1L);
        verify(contractRepository, times(1)).save(contract);
        verifyNoMoreInteractions(contractRepository);
    }

    @Test
    void transferPlayerWithContract() throws IllegalTransferException {
        Player player = new Player("John", "Doe", 24, 10);
        player.setId(1L);
        Team oldTeam = new Team("Barcelona", "Spain", 5, 100000.0);
        oldTeam.setId(10L);
        Contract oldContract = new Contract(player, oldTeam, 20000.0);
        Team newTeam = new Team("Real Madrid", "Spain", 3,200000.0);
        newTeam.setId(11L);
        when(contractRepository.findByPlayerIdAndActiveTrue(1L)).thenReturn(oldContract);

        Contract contract = contractService.transferPlayer(player, newTeam);

        verify(contractRepository).findByPlayerIdAndActiveTrue(1L);
        assertAll("Check contracts details",
                () -> assertNotNull(contract),
                () -> assertEquals(newTeam, contract.getTeam()),
                () -> assertEquals(player, contract.getPlayer()),
                () -> assertTrue(contract.getContractFee() > 0),
                () -> assertFalse(oldContract.getActive()),
                () -> assertTrue(contract.getActive()));
        assertAll("Check balances changes",
                () -> assertTrue(oldTeam.getBalance() > 100000.0),
                () -> assertTrue(newTeam.getBalance() < 200000.0));
        // Check invocation order
        InOrder inOrder = inOrder(contractRepository);
        inOrder.verify(contractRepository).save(oldContract);
        inOrder.verify(contractRepository).save(contract);
    }

    @Test
    void transferPlayerIllegalTransferException() {
        Player player = new Player("John", "Doe", 24, 10);
        player.setId(1L);
        Team oldTeam = new Team("Barcelona", "Spain", 5, 100000.0);
        oldTeam.setId(10L);
        Contract oldContract = new Contract(player, oldTeam, 20000.0);
        when(contractRepository.findByPlayerIdAndActiveTrue(1L)).thenReturn(oldContract);

        assertThrows(IllegalTransferException.class, () -> contractService.transferPlayer(player, oldTeam));
        verify(contractRepository).findByPlayerIdAndActiveTrue(1L);
    }

    @Test
    void calculateContractFee() {
        Player player = new Player("John", "Doe", 22, 20);
        Team team = new Team("Wales", "UK", 10, 200000.0);
        Double calculatedFee = contractService.calculateContractFee(player, team);
        double transferFee = 1.0 * player.getExperience() * 100000 / player.getAge();
        double teamCommission = 1.0 * team.getCommissionPercent() / 100 * transferFee;
        double contractFee = transferFee + teamCommission;
        assertEquals(0, calculatedFee.compareTo(contractFee));
    }

    @Test
    void getContractsByPlayerId() {
        Player player = new Player("John", "Doe", 24, 10);
        Team barcelona = new Team("Barcelona", "Spain", 5, 100000.0);
        Team real = new Team("Real Madrid", "Spain", 3,200000.0);
        Contract contractBarcelona = new Contract(player, barcelona, 200000.0);
        Contract contractReal = new Contract(player, real, 10000.0);
        when(contractRepository.findByPlayerId(1L)).thenReturn(Arrays.asList(contractBarcelona, contractReal));

        List<Contract> contracts = contractService.getContractsByPlayerId(1L);
        List<Contract> notExistingContracts = contractService.getContractsByPlayerId(2L);

        assertNotNull(contracts);
        assertNotNull(notExistingContracts);
        assertEquals(2, contracts.size());
        assertTrue(contracts.contains(contractBarcelona));
        assertTrue(contracts.contains(contractReal));
        assertEquals(0, notExistingContracts.size());
    }

    @Test
    void getTeamsByPlayerId() {
        Player player = new Player("John", "Doe", 24, 10);
        Team barcelona = new Team("Barcelona", "Spain", 5, 100000.0);
        Team real = new Team("Real Madrid", "Spain", 3,200000.0);
        Contract contractBarcelona = new Contract(player, barcelona, 200000.0);
        Contract contractReal = new Contract(player, real, 10000.0);
        when(contractRepository.findByPlayerId(1L)).thenReturn(Arrays.asList(contractBarcelona, contractReal));

        List<Team> teams = contractService.getTeamsByPlayerId(1L);
        List<Team> notExistingTeams = contractService.getTeamsByPlayerId(2L);

        assertNotNull(teams);
        assertNotNull(notExistingTeams);
        assertEquals(2, teams.size());
        assertTrue(teams.contains(barcelona));
        assertTrue(teams.contains(real));
        assertEquals(0, notExistingTeams.size());
    }

    @Test
    void getContractById() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(new Contract(null, null, 0.0)));

        assertNotNull(contractService.getContractById(1L).orElse(null));
        assertEquals(Optional.empty(), contractService.getContractById(2L));
    }

    @Test
    void transferFunds() throws NonSufficientFundsException {
        Team barcelona = new Team("Barcelona", "Spain", 5, 100000.0);
        Team real = new Team("Real Madrid", "Spain", 3,200000.0);

        contractService.transferFunds(barcelona, real, 50000);

        assertEquals(0, barcelona.getBalance().compareTo(50000.0));
        assertEquals(0, real.getBalance().compareTo(250000.0));
        verify(teamService).update(barcelona);
        verify(teamService).update(real);
    }

    @Test
    void transferFundsNonSufficientFundsException() {
        Team barcelona = new Team("Barcelona", "Spain", 5, 100000.0);
        Team real = new Team("Real Madrid", "Spain", 3,200000.0);

        assertThrows(NonSufficientFundsException.class, () -> contractService.transferFunds(barcelona, real, 120000));
        assertEquals(0, barcelona.getBalance().compareTo(100000.0));
        assertEquals(0, real.getBalance().compareTo(200000.0));
        verifyNoInteractions(teamService);
    }

}