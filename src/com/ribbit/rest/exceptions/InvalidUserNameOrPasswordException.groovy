package com.ribbit.rest.exceptions

/**
 * Thrown when a login attempt fails
 */
public class InvalidUserNameOrPasswordException extends RibbitException {

    private static final long serialVersionUID = 265599051885098292L

    /**
     * Creates a new instance of this exception
     */
    public InvalidUserNameOrPasswordException() {
        super("Invalid user name or password", HttpURLConnection.HTTP_UNAUTHORIZED)
    }

}
