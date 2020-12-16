package com.betbull.market;

import com.betbull.market.model.Player;
import com.betbull.market.model.Team;
import com.betbull.market.service.ContractService;
import com.betbull.market.service.PlayerService;
import com.betbull.market.service.TeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * The MarketCommandLineRunner is used to fill the database with testing data.
 */
@Component
public class MarketCommandLineRunner implements CommandLineRunner {

    private TeamService teamService;

    private PlayerService playerService;

    private ContractService contractService;

    /**
     * Instantiates a new MarketCommandLineRunner.
     *
     * @param teamService     the team service
     * @param playerService   the player service
     * @param contractService the contract service
     */
    public MarketCommandLineRunner(TeamService teamService, PlayerService playerService, ContractService contractService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.contractService = contractService;
    }

    @Override
    public void run(String... args) throws Exception {
        Team wales = teamService.create(new Team("Wales", "UK", 7, BigDecimal.valueOf(200000.0)));
        Team barcelona = teamService.create(new Team("Barcelona", "Spain", 10, BigDecimal.valueOf(150000.0)));
        Team atalanta = teamService.create(new Team("Atalanta", "Italy", 9, BigDecimal.valueOf(190000.0)));

        Player john = playerService.create(new Player("John", "Doe", 25, 24));
        Player mike = playerService.create(new Player("Mike", "Renault", 28, 11));
        Player oussama = playerService.create(new Player("Oussama", "Safi", 24, 12));
        Player patrice = playerService.create(new Player("Patrice", "Lamarque", 21, 5));
        Player omar = playerService.create(new Player("Omar", "Bouraus", 29, 7));
        Player samuel = playerService.create(new Player("Samuel", "Aidi", 31, 18));
        Player diego = playerService.create(new Player("Diego", "Chaieb", 23, 34));
        Player jacob = playerService.create(new Player("Jacob", "Nmiri", 20, 28));
        Player houssem = playerService.create(new Player("Houssem", "Denarie", 25, 33));

        contractService.transferPlayer(john, wales);
        contractService.transferPlayer(mike, wales);
        contractService.transferPlayer(oussama, wales);

        contractService.transferPlayer(patrice, barcelona);
        contractService.transferPlayer(omar, barcelona);
        contractService.transferPlayer(samuel, barcelona);

    }
}
