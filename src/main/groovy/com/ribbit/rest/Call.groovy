package com.ribbit.rest

import com.ribbit.rest.constants.CallStatus
import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.exceptions.RibbitException
import com.ribbit.rest.oauth.SignedRequest
import com.ribbit.rest.util.json.JSONException
import com.ribbit.rest.util.json.JSONObject
import com.ribbit.rest.util.Util
import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 11:21:58 AM
 */
class Call extends Resource {
    CallStatus status
    List<CallLeg> legs = new ArrayList<CallLeg>()
    def callURI
    def callID
    String callerId
    String mode
    String announce
    DateTime startTime
    DateTime endTime
    String duration
    boolean success
    boolean active
    boolean recording
    boolean outbound
    List<String> recordings

    private Call() { }

    public Call(RibbitConfig config) {
        this.config = config
        legs = new ArrayList<CallLeg>()
    }

    public void addLeg(CallLeg leg) {
        legs.add(leg)
    }

    public startCall() {
        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }
	def exceptions = []
        def req = new SignedRequest(config)
        def vars = new JSONObject()
        try {
            def legIds = legs.collect {it.id}
            vars.put("legs", legIds)
            if (!Util.isValidStringIfDefined(callerId)) {
                vars.put("callerid", callerId)
                exceptions.add("When defined, callerid must be a string of one or more characters");
            }
            if (!Util.isValidStringIfDefined(mode)) {
                vars.put("mode", mode);
                exceptions.add("When defined, mode must be a string of one or more characters");
            }
            if (!Util.isValidStringIfDefined(announce)) {
                vars.put("announce", announce);
                exceptions.add("When defined, announce must be a string of one or more characters");
            }
            if (exceptions.size() > 0) {
                throw new IllegalArgumentException(exceptions.join(";"));
            }

        } catch (JSONException ex) {
            throw new RibbitException("ha ha")
        }

        String uri = "calls/${userId}"
        def result = req.post(vars, uri)
        def temp = result.split("/")
        callID = temp[temp.size() - 1]
        callURI = result
    }

    public getCallInfo() {
        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        def req = new SignedRequest(config)
        def vars = new JSONObject()
        try {
            def legIds = legs.collect {it.id}
            vars.put("legs", legIds)


        } catch (JSONException ex) {
            throw new RibbitException("ha ha")
        }

        String uri = "calls/${userId}/callURI"
        def result = req.get(vars, uri)

        return result
    }
}
