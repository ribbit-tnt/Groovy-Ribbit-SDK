package com.ribbit.rest.oauth

import com.ribbit.rest.RibbitConfig
import com.ribbit.rest.util.NameValuePair
import com.ribbit.rest.util.OAuthUtility
import com.ribbit.rest.util.json.JSONObject
import java.util.logging.Logger
import com.ribbit.rest.exceptions.*
import java.util.logging.Level

/**
 * Signs and send requests to the Ribbit REST server. Also holds user
 * credentials.
 */
public final class SignedRequest {

    private static final Logger LOG = Logger.getLogger(SignedRequest.class.getName())
    public static final String ENCODING = "UTF-8"

    public static int EXECUTION_FAILURE = 418
    public static String ACCEPT_APPLICATION_JSON = "application/json"
    public static String CONTENT_APPLICATION_JSON = "application/json"
    public static String ACCEPT_APPLICATION_OCTET = "application/octet-stream"
    public static String CONTENT_APPLICATION_OCTET = "application/octet-stream"
    public static String ACCEPT_AUDIO_MPEG = "audio/mpeg"
    public static String ACCEPT_AUDIO_WAV = "audio/wav"
    private RibbitConfig config = null

    public SignedRequest(RibbitConfig config) {
        this.config = config
        LOG.setLevel(Level.INFO)
    }

    /**
     * Used when sending a 'GET' request to the Ribbit Server
     *
     * @param uri
     *            A Relative URI
     * @return The response
     */
    public String get(String uri) throws RibbitException {
        return send(uri, "GET", null, null, null, null, getAcceptType(uri), null, null)
    }

    /**
     * Used when sending a 'GET' request to the Ribbit Server, and saving the
     * response to a file.
     *
     * @param uri
     *            A Relative URI
     * @param stream
     *            An open OutputStream to which the response will be written
     * @param acceptType
     *            Included as a header in the request to the Ribbit Server
     */
    public String getToFile(String uri, OutputStream stream) throws RibbitException {
        if (stream == null) {
            throw new RibbitException("An open OutputStream must be supplied")
        }

        return send(uri, "GET", null, null, null, stream,
                uri.endsWith(".mp3") && uri.contains("call:") ? ACCEPT_AUDIO_MPEG : ACCEPT_APPLICATION_OCTET, null,
                null)
    }

    /**
     * Used when sending a 'GET' request to the Ribbit Server, and saving the
     * response to a file.
     *
     * @param uri
     *            A Relative URI
     * @return a Full URI that can be passed to a Media player
     */
    public String getStreamableUrl(String uri) throws RibbitException {
        uri = (uri.startsWith("http")) ? uri : config.getRibbitEndpoint() + uri
        ArrayList<NameValuePair> headers = createHeaders(uri, "GET", null, null, null, getAcceptType(uri))
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < headers.size(); i++) {
            if (i > 0) {
                sb.append("|")
            }
            NameValuePair header = headers.get(i)
            sb.append(header.getName() + "=" + header.getValue())
        }
        return uri + "?h=" + OAuthUtility.percentEncode(sb.toString())
    }

    /**
     * Used when sending a 'DELETE' request to the Ribbit Server
     *
     * @param uri
     *            A Relative URI
     */
    public void delete(String uri) throws RibbitException {
        send(uri, "DELETE", null, null, null, null, null, null, null)
    }

    /**
     * Used when sending a 'POST' request to the Ribbit Server
     *
     * @param vars
     *            A JSON object containing data to 'POST'
     * @param uri
     *            A Relative URI
     * @return The value of the returned 'Location' header
     */
    public String post( vars, String uri) throws RibbitException {
        return send(uri, "POST", vars, null, null, null, null, CONTENT_APPLICATION_JSON, null)
    }

    /**
     * Used when sending a 'POST' request to the Ribbit Server
     *
     * @param uri
     *            A Relative URI
     * @return The value of the returned 'Location' header
     */
    public String post(String uri) throws RibbitException {
        return send(uri, "POST", null, null, null, null, null, CONTENT_APPLICATION_JSON, null)
    }

    /**
     * Used when sending a 'POST' request to the Ribbit Server and logging in
     *
     * @param uri
     *            A Relative URI
     * @param xAuthUserName
     *            A user name to log in with
     * @param xAuthPassword
     *            A password to log in with
     * @return The value of the returned 'Location' header
     */
    public String post(String uri, String xAuthUserName, String xAuthPassword) throws RibbitException {
        return send(uri, "POST", null, xAuthUserName, xAuthPassword, null, null, null, null)
    }

    public String postFile(InputStream fileStream, String uri) throws RibbitException {
        return send(uri, "POST", null, null, null, null, null, CONTENT_APPLICATION_OCTET, fileStream)
    }

    /**
     * Used when sending a 'PUT' request to the Ribbit Server
     *
     * @param vars
     *            A JSON object containing data to 'POST'
     * @param uri
     *            A Relative URI
     * @return The response from the Ribbit Server
     */
    public String put(JSONObject vars, String uri) throws RibbitException {
        return send(uri, "PUT", vars, null, null, null, ACCEPT_APPLICATION_JSON, CONTENT_APPLICATION_JSON, null)
    }

    protected ArrayList<NameValuePair> createHeaders(String uri, String method, byte[] requestBody,
                                                     String xAuthUserName, String xAuthPassword, String acceptType) throws RibbitException {
        List<NameValuePair> customHeaders = config.getCustomHeaders()
        ArrayList<NameValuePair> headers = null
        if (customHeaders != null) {
            headers = new ArrayList<NameValuePair>(customHeaders)
        } else {
            headers = new ArrayList<NameValuePair>()
        }

        headers.add(createAuthHeader(uri, method, requestBody, xAuthUserName, xAuthPassword))
        headers.add(new NameValuePair("User-Agent", "ribbit_groovy_library_0.0.1"))
        headers.add(new NameValuePair("Accept", (acceptType == null) ? "application/json" : acceptType))
        return headers
    }

    private NameValuePair createAuthHeader(String uri, String method, byte[] requestBody, String xAuthUserName,
                                           String xAuthPassword) throws RibbitException {
        String bodySignature = ""
        String bodySignatureHeader = ""
        if (requestBody != null) {
            bodySignature = signForOAuth(requestBody)
            bodySignatureHeader = ", xoauth_body_signature_method=\"HMAC-SHA1\", xoauth_body_signature=\"" +
            OAuthUtility.percentEncode(bodySignature) + "\""
        }
        String normalizedUrl = OAuthUtility.normalizeURL(uri)
        String nonce = UUID.randomUUID().toString()
        String timestamp = Long.toString(System.currentTimeMillis())

        SortedMap<String, NameValuePair> oAuthParameters = OAuthUtility.populateMapForQueryString(uri)
        oAuthParameters.put("oauth_consumer_key", new NameValuePair("oauth_consumer_key", config.consumerKey))
        oAuthParameters.put("oauth_nonce", new NameValuePair("oauth_nonce", nonce))
        oAuthParameters.put("oauth_signature_method", new NameValuePair("oauth_signature_method", "HMAC-SHA1"))
        oAuthParameters.put("oauth_timestamp", new NameValuePair("oauth_timestamp", timestamp))
        if (bodySignature.length() > 0) {
            oAuthParameters.put("xoauth_body_signature", new NameValuePair("xoauth_body_signature", bodySignature))
            oAuthParameters.put("xoauth_body_signature_method", new NameValuePair("xoauth_body_signature_method",
                    "HMAC-SHA1"))
        }
        if (xAuthUserName != null) {
            oAuthParameters.put("x_auth_username", new NameValuePair("x_auth_username", xAuthUserName))
        }
        if (xAuthPassword != null) {
            oAuthParameters.put("x_auth_password", new NameValuePair("x_auth_password", xAuthPassword))
        }

        if (config.getAccessToken() != null) {
            oAuthParameters.put("oauth_token", new NameValuePair("oauth_token", config.accessToken))
        }

        StringBuffer paramBuffer = new StringBuffer()
        int i = 0
        for (Map.Entry<String, NameValuePair> entry: oAuthParameters.entrySet()) {
            if (i > 0) {
                paramBuffer.append("&")
            }
            NameValuePair nvp = entry.getValue()
            paramBuffer.append(nvp.toString())
            i++
        }
        String stringToSign = method + "&" + OAuthUtility.percentEncode(normalizedUrl) + "&" + OAuthUtility.percentEncode(paramBuffer.toString())
        String signature = signForOAuth(stringToSign)

        StringBuffer ah = new StringBuffer()
        ah.append("OAuth realm=\"%22http%3A%2F%2Foauth.ribbit.com%22\"")
        ah.append(", oauth_consumer_key=\"" + config.consumerKey + "\"")
        ah.append(", oauth_signature_method=\"HMAC-SHA1\"")
        ah.append(", oauth_timestamp=\"" + timestamp + "\"")
        ah.append(", oauth_nonce=\"" + nonce + "\"")
        ah.append(", oauth_signature=\"" + OAuthUtility.percentEncode(signature) + "\"")
        if (config.getAccessToken() != null) {
            ah.append(", oauth_token=\"" + config.getAccessToken() + "\"")
        }
        ah.append(bodySignatureHeader)
        if (xAuthUserName != null) {
            ah.append(", x_auth_username=\"" + xAuthUserName + "\"")
        }
        if (xAuthPassword != null) {
            ah.append(", x_auth_password=\"" + xAuthPassword + "\"")
        }
        return new NameValuePair("Authorization", ah.toString())
    }

    private String send(String uri, String method,  vars, String xAuthUserName, String xAuthPassword,
                        OutputStream stream, String acceptType, String contentType, InputStream fileToUpload)
    throws RibbitException {
        if (config.isExpired()) {
            throw new SessionExpiredException(config.accountId)
        }
        String fullUri = (uri.startsWith("http")) ? uri : config.getRibbitEndpoint() + uri
        boolean hasBody = method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")
        byte[] requestBody = null
        ByteArrayOutputStream fileOutStream = new ByteArrayOutputStream()
        byte[] buffer = new byte[8192]
        int bytesRead

        if (hasBody) {
            if (vars != null) {
                String body = vars.toString()
                try {
                    requestBody = body.getBytes(ENCODING)
                } catch (UnsupportedEncodingException e) {
                }
                // log.Append("\r\nBody - " + body )
            } else if (fileToUpload != null) {
                try {
                    while ((bytesRead = fileToUpload.read(buffer)) != -1) {
                        fileOutStream.write(buffer, 0, bytesRead)
                    }

                    requestBody = fileOutStream.toByteArray()
                    fileToUpload.close()
                    fileOutStream.close()
                    // log.Append("\r\nBody is a file of " + requestBody.Length
                    // + " bytes")
                } catch (IOException ex) {
                    throw new RibbitException("FileStream passed to upload was not readable", 0)
                }
            }
        }

        // create the HTTPRequest
        HttpURLConnection req = null
        try {
            req = (HttpURLConnection) new URL(fullUri).openConnection()
            req.setRequestMethod(method)
        } catch (ProtocolException e) {
            throw new RibbitException("Unknown method type", HttpURLConnection.HTTP_BAD_METHOD)
        } catch (IOException e) {
            throw new RibbitException("Invalid URI", EXECUTION_FAILURE)
        }

        req.setInstanceFollowRedirects(true)
        req.setUseCaches(false)
        req.setDoInput(true)

        StringBuilder headersString = new StringBuilder()
        ArrayList<NameValuePair> headers = createHeaders(fullUri, method, requestBody, xAuthUserName, xAuthPassword,
                acceptType)
        for (int i = 0; i < headers.size(); i++) {
            NameValuePair n = headers.get(i)
            req.addRequestProperty(n.getName(), n.getValue())
            headersString.append(n.getName() + ": " + n.getValue() + "\n")
        }

        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
            if (requestBody != null && requestBody.length > 0) {

                String requestContentType = (contentType == null) ? CONTENT_APPLICATION_JSON : contentType
                req.setDoOutput(true)
                req.setRequestProperty("Content-Type", requestContentType)
                DataOutputStream out = null
                try {
                    LOG.info(String.format("Signed Request %s to %s\nHeaders:%s\n%s", method, fullUri, headersString,
                            requestBody))
                    out = new DataOutputStream(req.getOutputStream())
                    out.write(requestBody)
                    out.flush()
                    out.close()
                } catch (IOException e) {
                    req.disconnect()
                    throw new RibbitException("Unable to write request body to output stream.", EXECUTION_FAILURE)
                }
            }
        }

        int status = 0
        String output = ""
        BufferedReader reader = null
        InputStream inputStream = null
        try {
            config.noteUsed()
            inputStream = req.getInputStream()
        } catch (IOException e) {
            inputStream = req.getErrorStream()
        }

        try {
            if (inputStream != null) {
                if (stream != null) {
                    int i = 0
                    while ((i = inputStream.read()) != -1) {
                        stream.write(i)
                    }
                } else {
                    reader = new BufferedReader(new InputStreamReader(inputStream))
                    if (reader != null) {

                        StringBuilder sb = new StringBuilder()
                        output = reader.readLine()
                        while (output != null) {
                            sb.append(output)
                            output = reader.readLine()
                        }
                        output = sb.toString()
                    }
                }
            }
            status = req.getResponseCode()
        } catch (IOException ex) {
            throw new RibbitException("The server returned no http status code", EXECUTION_FAILURE)
        }

        if (req.getHeaderField("Location") != null) {
            output = req.getHeaderField("Location")
        }
        req.disconnect()
        if (status == 0 || status >= 400) {
            LOG.warning(String.format("Response from %s\nStatus: %s\n%s", fullUri, status, output))
        } else {
            LOG.info(String.format("Response from %s\nStatus: %s\n%s", fullUri, status, output))
        }
        switch (status) {
            case HttpURLConnection.HTTP_BAD_REQUEST:
                String errorMessage = output != null ? output : "The request was malformed"
                throw new RibbitException(errorMessage, status)
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                throw new NotAuthorizedException()
            case HttpURLConnection.HTTP_CONFLICT:
                //errorMessage = output != null ? output : uri + " already exists"
                throw new ResourceConflictException('Resource already exists')
            case HttpURLConnection.HTTP_NOT_FOUND:
                throw new ResourceNotFoundException(output)
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                throw new RibbitException("An unexpected error occured on the server: " + output, status)
            case HttpURLConnection.HTTP_NO_CONTENT:
            case HttpURLConnection.HTTP_ACCEPTED:
            case HttpURLConnection.HTTP_CREATED:
            case HttpURLConnection.HTTP_OK:
                break
            default:
                if (output == null) {
                    throw new RibbitException("The server returned an unexpected result", status)
                }
                break
        }
        if (stream != null) {
            output = ""
        }

        return output
    }

    private String signForOAuth(String textToSign) throws RibbitException {
        String secret = OAuthUtility.percentEncode(config.getSecretKey()) + "&" +
            OAuthUtility.percentEncode(config.getAccessSecret())
        return OAuthUtility.calculateHMACSHA1(textToSign, secret)
    }

    private String signForOAuth(byte[] body) throws RibbitException {
        String secret = OAuthUtility.percentEncode(config.getSecretKey()) + "&" +
            OAuthUtility.percentEncode(config.getAccessSecret())
        return OAuthUtility.calculateHMACSHA1(body, secret)
    }

    private String getAcceptType(String uri) {
        String acceptType = ACCEPT_APPLICATION_JSON
        if (uri.endsWith(".mp3")) {
            acceptType = ACCEPT_AUDIO_MPEG
        } else if (uri.endsWith(".wav")) {
            acceptType = ACCEPT_AUDIO_WAV
        } else if (uri.endsWith(".txt")) {
            acceptType = ACCEPT_APPLICATION_OCTET
        }
        return acceptType
    }
}
