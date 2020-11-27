package com.betbull.market.exception;

/**
 * The NonSufficientFundsException is used to indicate that the team doesn't have enough funds for transfer.
 */
public class NonSufficientFundsException extends IllegalTransferException {

    /**
     * Instantiates a new Non sufficient funds exception.
     *
     * @param message the message
     */
    public NonSufficientFundsException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Non sufficient funds exception.
     *
     * @param cause the cause
     */
    public NonSufficientFundsException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Non sufficient funds exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public NonSufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
