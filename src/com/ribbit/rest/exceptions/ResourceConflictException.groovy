package com.ribbit.rest.exceptions

/**
 * Thrown when a request is made to create a resource, such as a user, and there is a conflict
 */
public class ResourceConflictException extends RibbitException {
    private static final long serialVersionUID = -6296613759892421709L

    /**
     * Creates a new instance of this exception
     * @param uri The resource which already exists
     */
    public ResourceConflictException(String message) {
        super (message, HttpURLConnection.HTTP_CONFLICT)
    }

}
