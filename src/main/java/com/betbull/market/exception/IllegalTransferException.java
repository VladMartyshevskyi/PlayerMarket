package com.betbull.market.exception;

/**
 * The IllegalTransferException is used to indicate error while transferring a player between teams.
 */
public class IllegalTransferException extends Exception {

    /**
     * Instantiates a new Illegal transfer exception.
     *
     * @param message the message
     */
    public IllegalTransferException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Illegal transfer exception.
     *
     * @param cause the cause
     */
    public IllegalTransferException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Illegal transfer exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public IllegalTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
