package com.betbull.market.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Player represents football player.
 */
@Entity
@Data
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "age is mandatory")
    @Min(value = 12, message = "Min age is 12 y.o")
    @Max(value = 70, message = "Max age is 70 y.o")
    private Integer age;

    @Column(nullable = false)
    @NotNull(message = "experience is mandatory")
    @Min(value = 0, message = "Min experience is 0")
    @Max(value = 600, message = "Max experience is 600")
    private Integer experience;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contract> contracts = new ArrayList<>();

    /**
     * Instantiates a new Player.
     *
     * @param firstName  the first name
     * @param lastName   the last name
     * @param age        the age
     * @param experience the experience
     */
    public Player(String firstName, String lastName, Integer age, Integer experience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.experience = experience;
    }

}
