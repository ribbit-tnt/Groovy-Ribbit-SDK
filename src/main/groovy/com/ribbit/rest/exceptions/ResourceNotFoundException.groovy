package com.ribbit.rest.exceptions

/**
 * Thrown when a request is made, but the affected resource is not found
 */
public class ResourceNotFoundException extends RibbitException {

    private static final long serialVersionUID = 5711483469811216978L

    /**
     * Creates a new instance of this exception
     */
    public ResourceNotFoundException() {
        super ("Resource not found", HttpURLConnection.HTTP_NOT_FOUND)
    }
    public ResourceNotFoundException(String message) {
        super (message == null ? "Resource not found" : message, HttpURLConnection.HTTP_NOT_FOUND)
    }
}
