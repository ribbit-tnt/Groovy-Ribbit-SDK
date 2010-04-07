package com.ribbit.rest.exceptions

/**
 * Many exceptions that the Ribbit Java Library will throw will either be a RibbitException, or extend it.
 */
public class RibbitException extends RuntimeException {

    private static final long serialVersionUID = 3351634308963481487L
    private final int status


    /**
     * Creates a new instance of this exception
     * @param message An error message
     */
    public RibbitException(String message) {
        super(message)
        status = 0
    }

    /**
     * Creates a new instance of this exception
     * @param message An error message
     * @param httpStatus The http status returned from the server
     */
    public RibbitException(String message, int httpStatus) {
        super(message)
        status = httpStatus
    }

    /**
     * Returns a string representation of the exception
     */
    public String toString() {

        return ((status != 0) ? "http error code - " + Integer.toString(status) +" ":"") + this.getMessage()
    }

    /**
     * The HTTP Status returned from the server
     * @return the http status returned from the server, or zero if no request was made.
     */
    public int getStatus() {
        return status
    }
}
