package com.betbull.market.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * The type TransferDTO used to create transfers.
 */
@Data
@NoArgsConstructor
public class TransferDTO {
    @NotNull
    private Long playerId;
    @NotNull
    private Long teamId;
}
