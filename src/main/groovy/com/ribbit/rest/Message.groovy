package com.ribbit.rest

import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.exceptions.RibbitException
import com.ribbit.rest.oauth.SignedRequest
import com.ribbit.rest.util.Util
import com.ribbit.rest.util.json.JSONException
import com.ribbit.rest.util.json.JSONObject
import org.joda.time.DateTime
import com.google.gson.JsonParser
import com.google.gson.JsonObject
import com.ribbit.rest.deserializers.Utils

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 8, 2010
 * Time: 6:39:29 PM
 * To change this template use File | Settings | File Templates.
 */
class Message extends Resource {
    String title
    String body
    String userId
    String sender
    String id
    String from
    String mediaUri
    List<String> mediaItems = new ArrayList<String>()
    DateTime time
    Boolean newMessage
    Boolean urgentMessage
    String folder
    List<MessageDestination> recipients = new ArrayList<MessageDestination>()

    private Message() {}
    public Message(RibbitConfig config) {
        this.config = config
    }

    public void addRecipient(String recipient) {
        recipients.add(new MessageDestination(destination:recipient))
    }

    public String sendSMS() throws RibbitException {
        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        ArrayList<String> exceptions = new ArrayList<String>();

        if (!Util.isNonEmptyArray(recipients)) {
            exceptions.add("recipients is required");
        }
        if (!Util.isValidStringIfDefined(body)) {
            exceptions.add("When defined, body must be a string of one or more characters");
        }
        if (!Util.isValidStringIfDefined(sender)) {
            exceptions.add("When defined, sender must be a string of one or more characters");
        }
        if (!Util.isValidStringIfDefined(title)) {
            exceptions.add("When defined, title must be a string of one or more characters");
        }
        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"));
        }

        JSONObject vars = new JSONObject();
        try {
            def r = recipients.collect {it.destination}
            vars.put("recipients", r);

            if (Util.isDefined(body)) {
                vars.put("body", body);
            }

            if (Util.isDefined(sender)) {
                vars.put("sender", sender);
            }

            if (Util.isDefined(title)) {
                vars.put("title", title);
            }

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured creating a JSONObject");
        }
        def req = new SignedRequest(config)
        String uri = "messages/" + userId + "/outbox";
        String result = null;
        String serviceResult = req.post(vars, uri);

        id = Util.getIdFromUri(serviceResult);
    }

        public updateMessage(Map map /* String folder, Boolean newMessage, Boolean urgent, String newFolder*/)  throws RibbitException  {

        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        ArrayList<String> exceptions = new ArrayList<String>();

        if (!Util.isBoolIfDefined(map.newMessage)) {
            exceptions.add("When defined, newMessage must be boolean")
        }
        if (!Util.isBoolIfDefined(map.urgent)) {
            exceptions.add("When defined, urgent must be boolean")
        }
        if (!Util.isValidStringIfDefined(map.newFolder)) {
            exceptions.add("When defined, newFolder must be a string of one or more characters")
        }
        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"))
        }

        JSONObject vars = new JSONObject();
        try {

            if (Util.isDefined(map.newMessage)) {
                vars.put("new", map.newMessage)
            }

            if (Util.isDefined(map.urgent)) {
                vars.put("urgent", map.urgent)
            }

            if (Util.isDefined(map.newFolder)) {
                vars.put("folder", map.newFolder)
            }

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured creating a JSONObject");
        }

        def req = new SignedRequest(config)
        String uri = "messages/${userId}/${folder}/${id}"
        String serviceResult = req.put(vars, uri);

        try {
            def obj = ((JsonObject) new JsonParser().parse(serviceResult)).get("entry")
            def msg = Utils.deserialize(obj.toString(), Message.class)
            msg.properties.each {
                this.setProperty(it.key, it.value)
            }
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }
    }
}
