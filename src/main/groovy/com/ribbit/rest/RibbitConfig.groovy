package com.ribbit.rest;


import com.ribbit.rest.util.NameValuePair

/**
 * This class manages the current application and user settings.
 */
public class RibbitConfig {
    String username
    String application
    String accountId
    String domain
    String consumerKey
    String secretKey

    String requestToken
    String requestSecret

    String accessToken
    String accessSecret
    def expiredTime

    String endpoint

    List customHeaders

    Long accessTokenAllocatedTime
    Long accessTokenLastUsed

    /**
     * Sets the request token and secret
     * @param requestToken the request token
     * @param requestSecret the request secret
     */
    public void setRequestToken(String requestToken, String requestSecret) {
        if (requestToken == null) {
            this.requestToken = null
            this.requestSecret = null
        } else {
            this.requestToken = requestToken
            this.requestSecret = requestSecret
            this.accessToken = requestToken
            this.accessSecret = requestSecret
            this.accessTokenAllocatedTime = this.accessTokenLastUsed = System.currentTimeMillis()
        }
    }

    /**
     * Sets custom headers for each request to the Ribbit server. Clears any current custom headers
     * @param headers a NameValuePair list of headers.
     */
//    public void setCustomHeaders(List<NameValuePair> headers) {
//        setCustomHeaders(headers, true)
//    }

    /**
     * Clears any current custom headers
     */
    public void resetCustomHeaders() {
        session.setValue("customHeaders", new ArrayList<NameValuePair>())
    }
    /**
     * Removes a custom header
     * @param name The name of the header to be removed
     */
    @SuppressWarnings("unchecked")
    public void removeCustomHeader(String name) {
        customHeaders?.findAll {
            !it.getName().equalsIgnoreCase(name)
        }
    }

    /**
     * Sets custom headers for each request to the Ribbit server.
     * @param headers a NameValuePair list of headers.
     */
    @SuppressWarnings("unchecked")
    public void setCustomHeaders(List<NameValuePair> headers, boolean reset) {
        if (reset) {
            resetCustomHeaders()
        }
        List<NameValuePair> sessionHeaders = customHeaders ? customHeaders : new ArrayList<NameValuePair>()

        for (int i = 0; i < headers.size(); i++) {
            boolean add = true
            for (int j = 0 ;j < sessionHeaders.size(); j++) {
                if (headers.get(i).getName().equalsIgnoreCase((sessionHeaders.get(j).getName()))) {
                    sessionHeaders.set(j, headers.get(i))
                    add = false
                }
            }
            if (add) {
                sessionHeaders.add(headers.get(i))
            }
        }
        this.customHeaders = sessionHeaders
    }

    /**
     * Clears current user credentials
     */
    public void clearUser() {
        accountId = null
        username = null
        accessToken = null
        accessSecret = null
        requestToken = null
        requestSecret = null
    }

    /**
     * Sets user credentials
     *
     * @param userId A globally unique User identifier
     * @param userName The user name with which a user logs on
     * @param accessToken A current access token for a user session
     * @param accessSecret A current access secret for a user session
     */
    public void setUser(String userId, String userName, String accessToken, String accessSecret) {
        this.accountId = userId
        this.username = userName
        this.accessToken = accessToken
        this.accessSecret = accessSecret
        this.accessTokenAllocatedTime = this.accessTokenLastUsed = System.currentTimeMillis()

        this.requestToken = null
        this.requestSecret = null
    }

    /**
     * Sets application credentials. These can generally be found on http://developer.ribbit.com
     *
     * @param applicationId Your application Id,
     * @param consumerToken Your consumer token
     * @param secretKey A valid secret key
     * @param domain The domain your application is in.
     */
    public void setApplication(String applicationId, String consumerToken, String secretKey, String domain) {
        // TODO: Verify that Groovy truth is what is expected
        if (this.application != applicationId || this.secretKey != secretKey) {
            clearUser()
        }

        this.application = applicationId
        this.consumerKey = consumerToken
        this.secretKey = secretKey
        this.domain = domain
    }

    /**
     * The URI of the Ribbit REST server currently used
     * @return The URI of the Ribbit REST server currently used
     */
    public String getRibbitEndpoint() {
        if (!endpoint.endsWith("/")) {
            endpoint = endpoint + "/"
            setRibbitEndpoint(endpoint)
        }
        return endpoint
    }

    /**
     * Change the URI of the Ribbit REST server used
     */
    public void setRibbitEndpoint(String endpoint) {
        this.endpoint = endpoint
    }

    @SuppressWarnings("unchecked")
    public String getActiveUserId() {
        String output = this.accountId
        def obj = customHeaders?.findAll {
            !it.getName().equalsIgnoreCase("X-BT-Ribbit-SP-UserId")
        }
        if (obj)
            return obj.getValue()
        else return output
    }

    public boolean isExpired() {
        return accessToken !=null && expiredTime != null
    }

    public void noteUsed() {
        this.accessTokenLastUsed = System.currentTimeMillis()
    }
}