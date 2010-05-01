package com.ribbit.rest.exceptions

/**
 * Thrown when a request is made which the Ribbit Server rejects as authentication details are incorrect, or permission to perform the request is denied
 */
public class NotAuthorizedException extends RibbitException {
    private static final long serialVersionUID = 3653528218210445911L

    /**
     * Creates a new instance of this exception
     */
    public  NotAuthorizedException() {
        super ("The request was not authorized",HttpURLConnection.HTTP_UNAUTHORIZED)
    }

}
