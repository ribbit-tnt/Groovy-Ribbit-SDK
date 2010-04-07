package com.ribbit.rest.exceptions

/**
 * This exception is thrown when the user has been idle for an hour, or logged in for 24 hours
 * After 10 minutes or so, the session will be removed - attempts to use it after that will result in a session not found exception.
 */
public class SessionExpiredException extends RibbitException {
    private static final long serialVersionUID = 2623518218212934511L

    public SessionExpiredException(String sessionId) {
        super("Session " + sessionId + " has expired")
    }

}
