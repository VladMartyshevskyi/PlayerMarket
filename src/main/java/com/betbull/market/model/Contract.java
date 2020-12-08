package com.betbull.market.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The Contract represents a contract between football player and team.
 */
@Data
@NoArgsConstructor
@Entity
public class Contract {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Double contractFee;

    /**
     * Instantiates a new Contract.
     *
     * @param player        the player
     * @param team          the team
     * @param contractFee   the contractFee
     */
    public Contract(Player player, Team team, Double contractFee) {
        this.player = player;
        this.team = team;
        this.active = true;
        this.contractFee = contractFee;
    }
}
