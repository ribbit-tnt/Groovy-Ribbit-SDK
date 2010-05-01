package com.ribbit.rest

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ribbit.rest.deserializers.Utils
import com.ribbit.rest.exceptions.InvalidUserNameOrPasswordException
import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.exceptions.RibbitException
import com.ribbit.rest.oauth.SignedRequest
import com.ribbit.rest.util.Util
import com.ribbit.rest.util.json.JSONException
import java.lang.reflect.Type
import java.util.logging.Logger

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 2, 2010
 * Time: 10:33:46 PM
 */
class Ribbit {
    private static final Logger LOG = Logger.getLogger(Ribbit.class.getName())

    RibbitConfig config = null

    Ribbit(RibbitConfig config) {
        this.config = config
    }

    /**
     * Login a user
     *
     * @param login The user name with which a User is logging on
     * @param password The password with which a User is logging on
     * @return true if the User credentials are valid
     */
    public boolean login(String login, String password) throws RibbitException {
        logout()

        SignedRequest req = new SignedRequest(config)

        String response = null
        try {
            response = req.post("login", login, password)
        } catch (NotAuthorizedException e) {
            throw new InvalidUserNameOrPasswordException()
        }
        def temp = response.split('&')
        String accessToken = temp[0].split("=")[1]
        String accessSecret = temp[1].split("=")[1]
        String loggedInUserId = temp[2].split("=")[1]

        //save
        config.setUser(loggedInUserId, login, accessToken, accessSecret)
        return true
    }

    /**
     * Logoff a user.
     *
     */
    public void logout() throws RibbitException {
        config.clearUser()
    }

    /**
     * Returns true when there is an user currently authenticating on the Ribbit Mobile domain - normally while they have been redirected away from the application
     * @return true
     */
    public boolean isAuthenticationRequestActive() {
        return config.getRequestToken() != null && config.getRequestToken() != ""
    }

    /**
     * Asks the REST server to create an authentication request token, and returns a URL to which a user should navigate in order to approve it
     * @param callbackUrl A URL to which the user should be returned
     * @return A url to which a user should navigate to approve the application
     */
    public String createUserAuthenticationUrl(String callbackUrl) {
        logoff()
        String response = new SignedRequest(config).post("request_token")
        String requestToken = response.split("&")[0].split("=")[1]
        String requestSecret = response.split("&")[1].split("=")[1]

        config.setRequestToken(requestToken, requestSecret)

        String callbackQueryParam = (callbackUrl != null) ? "&oauth_callback=" + Util.redirectUriBuilder(callbackUrl) : ""

        return config.getRibbitEndpoint() + "oauth/display_token.html?oauth_token=" + requestToken + callbackQueryParam
    }

    /**
     * Checks a previously created request token to see if has been accepted, and if so, configures the user session to use a valid access token and secret. At this point, Ribbit.isLoggedIn will be true.
     * @return true if the user has accepted the request, false or an exception if they have not completed the approval
     * @throws RibbitException
     */
    public boolean checkAuthenticatedUser() throws RibbitException {

        if (!isAuthenticationRequestActive()) {
            throw new RibbitException("No active authentication request can be found")
        }

        String response = new SignedRequest(config).post("access_token")

        String accessToken = response.split("&")[0].split("=")[1]
        String accessSecret = response.split("&")[1].split("=")[1]
        String userId = response.split("&")[2].split("=")[1]

        String userDetails = ""
        String domain = ""
        String userName = ""
        try {
            userDetails = URLDecoder.decode(response.split("&")[3], "UTF-8")
            domain = userDetails.split(":")[0]
            userName = userDetails.split(":")[1]
        } catch (UnsupportedEncodingException e) {
            LOG.warning(String.format("Could not determine user from access token details, %s", response))
        }

        config.setDomain(domain)
        config.setRequestToken(null, null)
        config.setUser(userId, userName, accessToken, accessSecret)
        return true
    }

    public Call newCall() {
        return new Call(config)
    }

    public Message newMessage() {
        return new Message(config)
    }

    public Folder getFolder(String name) {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException();
        }
        String userId = config.getActiveUserId();

        if (!Util.isValidString(name)) {
            throw new IllegalArgumentException("folder is required");
        }

        def req = new SignedRequest(config)
        def uri = "media/${config.getDomain()}/${name}"
        String serviceResult = req.get(uri);
        def folder

        try {
            folder = Utils.deserialize(serviceResult, Folder.class)
            folder.folderName = name
            folder.config = config
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }

        return folder;
    }

    public Folder createFolder(name) throws RibbitException {

        if (config.getAccountId() == null) {
            throw new NotAuthorizedException();
        }
        String userId = config.getActiveUserId();

        if (!Util.isValidString(name)) {
            throw new IllegalArgumentException("folder name is required");
        }

        JsonObject vars = new JsonObject();
        try {
            vars.addProperty("id", name);

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured creating a JSONObject")
        }

        def req = new SignedRequest(config)
        String uri = "media/${config.getDomain()}"
        String result = null;
        String serviceResult = req.post(vars, uri);

        return getFolder(name)
    }

    def getDevices() throws RibbitException {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException();
        }
        String userId = config.getActiveUserId();

        def req = new SignedRequest(config)
        String uri = "devices/${userId}"

        String serviceResult = req.get(uri);

        try {
            Type listType = new TypeToken<Collection<Device>>() {}.getType()
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).getAsJsonArray("entry")
            def result = (List) Utils.deserialize(obj.toString(), listType)
            return result//.asImmutable()
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }


    }

    public Application getApplication() throws RibbitException {

        ArrayList<String> exceptions = new ArrayList<String>();

        if (!Util.isValidStringIfDefined(domain)) {
            exceptions.add("When defined, domain must be a string of one or more characters");
        }
        if (!Util.isValidStringIfDefined(applicationId)) {
            exceptions.add("When defined, applicationId must be a string of one or more characters");
        }
        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"));
        }
        String domainValue = Util.isDefined(domain) ? domain : config.getDomain();
        String applicationIdValue = Util.isDefined(applicationId) ? applicationId : config.getApplicationId();
        String uri = "apps/" + domainValue + ":" + applicationIdValue;
        //   ApplicationResource result = null;
        String serviceResult = signedRequest.get(uri);

        try {

            //     result = new ApplicationResource((new JSONObject(serviceResult)).getJSONObject("entry"));

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }
        return result;

    }

    Device createDevice(attributes) {

    }

    Device getDevice(String deviceId) {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        def req = new SignedRequest(config)
        String uri = "devices/${userId}/${deviceId}"
        String serviceResult = req.get(uri);

        try {
            def obj = new JsonParser().parse(serviceResult).getAt("entry")
            def result = Utils.deserialize(obj.toString(), Device.class)
            return result
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }
    }

    List getServices() {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        def req = new SignedRequest(config)
        String uri = "services/${userId}"
        String serviceResult = req.get(uri)

        try {
            Type listType = new TypeToken<Collection<Service>>() {}.getType()
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).getAsJsonArray("entry")
            def result = (List) Utils.deserialize(obj.toString(), listType)
            result.each {
                it.config = config
            }
            return result
        } catch (JSONException e) {

        }
    }

    User getUser(String userId) {

        ArrayList<String> exceptions = new ArrayList<String>();

        if (!Util.isValidString(userId)) {
            exceptions.add("userId is required");
        }
        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(Util.join(exceptions, ";"));
        }

        def req = new SignedRequest(config)
        String uri = "users/${userId}"
        String serviceResult = signedRequest.get(uriToCall);

        def result
        try {
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).getAsJsonArray("entry")
            result = Utils.deserialize(obj.toString(), User.class)

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage())
        }
        return result

    }

    List getUsers() {
        def req = new SignedRequest(config)
        String q = Util.createQueryString(null, null, null, null);
        String uri = "users/${q}"
        String serviceResult = req.get(uri)

        try {
            Type listType = new TypeToken<Collection<User>>() {}.getType()
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).getAsJsonArray("entry")
            def result = (List) Utils.deserialize(obj.toString(), listType)
            result.each {
                it.config = config
            }
            return result
        } catch (JSONException e) {
            e.printStackTrace()
        }
    }

    Call getCall(String callId) {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()
        if (!Util.isValidString(callId)) {
            throw new IllegalArgumentException("callId is required")
        }
        def req = new SignedRequest(config)
        String uri = "calls/${userId}/${callId}"
        def call
        String serviceResult = req.get(uri)
        try {
            def obj = new JsonParser().parse(serviceResult).getAt("entry")
            call = Utils.deserialize(obj.toString(), Call.class)
            call.config = config
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }
        return call
    }

    List getCalls(Map map) {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        def exceptions = new ArrayList<String>()

        String pagingParamError = Util.checkPagingParameters(map?.startIndex, map?.count);
        if (pagingParamError != null) {
            exceptions.add(pagingParamError)
        }

        String filterParamError = Util.checkFilterParameters(map?.filterBy, map?.filterValue);
        if (filterParamError != null) {
            exceptions.add(filterParamError)
        }

        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"))
        }
        def req = new SignedRequest(config)
        String q = Util.createQueryString(map?.startIndex, map?.count, map?.filterBy, map?.filterValue);
        String uri = "calls/" + userId + q;
        String serviceResult = req.get(uri)

        try {
            Type listType = new TypeToken<Collection<Call>>() {}.getType()
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).getAsJsonArray("entry")
            def result = (List) Utils.deserialize(obj.toString(), listType)
            return result
        } catch (JSONException e) {
            e.printStackTrace()
        }

    }

    public Message getMessage(Map map) {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        ArrayList<String> exceptions = [ ]

        if (!Util.isValidString(map.messageId)) {
            exceptions.add("messageId is required");
        }
        if (!Util.isValidString(map.folder)) {
            exceptions.add("folder is required");
        }
        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"));
        }
        def req = new SignedRequest(config)
        String uriToCall = "messages/${userId}/${map.folder}/${map.messageId}"
        String serviceResult = req.get(uriToCall);

        try {
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).get("entry")
            def message = Utils.deserialize(obj.toString(), Message.class)
            return message
        } catch (JSONException e) {
            throw e
        }
    }

    public List getMessages(Map map) throws RibbitException {
if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        ArrayList<String> exceptions = new ArrayList<String>();

        String pagingParamError = Util.checkPagingParameters(map.startIndex, map.count)
        if (pagingParamError != null) {
            exceptions.add(pagingParamError)
        }

        String filterParamError = Util.checkFilterParameters(map.filterBy.toString(), map.filterValue)
        if (filterParamError != null) {
            exceptions.add(filterParamError)
        }

        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"))
        }

        def req = new SignedRequest(config)
        String q = Util.createQueryString(map.startIndex, map.count, map.filterBy.toString(), map.filterValue);
        String uri = "messages/${userId}" + q;
        String serviceResult = req.get(uri);

        try {

            Type listType = new TypeToken<Collection<Message>>() {}.getType()
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).getAsJsonArray("entry")
            def result = (List) Utils.deserialize(obj.toString(), listType)
            return result
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }

    }
}
