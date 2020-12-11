package com.betbull.market.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Team represents football team.
 */
@Data
@NoArgsConstructor
@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "title is mandatory")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "country is mandatory")
    private String country;

    @Column(nullable = false)
    @NotNull(message = "commissionPercent is mandatory")
    @Min(value = 0, message = "Min is 0 percents")
    @Max(value = 10, message = "Max is 10 percents")
    private Integer commissionPercent;

    @Column(nullable = false)
    @NotNull(message = "balance is mandatory")
    @PositiveOrZero(message = "balance should be 0 or positive value")
    private BigDecimal balance;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contract> contracts = new ArrayList<>();

    /**
     * Instantiates a new Team.
     *
     * @param title            the title
     * @param country          the country
     * @param commissionPercent the commision percent
     * @param balance          the balance
     */
    public Team(String title, String country, Integer commissionPercent, BigDecimal balance) {
        this.title = title;
        this.country = country;
        this.commissionPercent = commissionPercent;
        this.balance = balance;
    }

}
