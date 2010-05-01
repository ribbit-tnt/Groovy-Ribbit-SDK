package com.ribbit.rest

import com.ribbit.rest.constants.ServiceType
import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.exceptions.RibbitException
import com.ribbit.rest.util.json.JSONException
import com.ribbit.rest.util.json.JSONObject
import com.ribbit.rest.oauth.SignedRequest
import com.ribbit.rest.deserializers.Utils
import com.google.gson.JsonParser
import com.google.gson.JsonObject
import com.ribbit.rest.util.json.JSONArray
import com.google.gson.JsonArray

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 7, 2010
 * Time: 11:38:33 AM
 */
class Service extends Resource{
    String id
    boolean active
    String status
    boolean voicemail
    String serviceType
    List folders

    public Service() {}

    public Service(RibbitConfig config) {
        this.config = config
        folders = new ArrayList<String>()
    }

    @Override
    public void setServiceFolders(List<String> folders) throws RibbitException{
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        if (folders.isEmpty()) {
            throw new IllegalArgumentException("folders is required");
        }

        def req = new SignedRequest(config)
        def vars = new JSONObject()
        try {
            vars.put("folders", folders )
        } catch(JSONException ex) {
            throw new RibbitException("An unexpected error occured creating a JSONObject")
        }

        String uri = "services/${userId}/${id.toString().replace('\"','')}"
        String serviceResult = req.put(vars, uri);
        def obj =  new JsonParser().parse(serviceResult)
        
        def s = Utils.deserialize(obj.get("entry").toString(),Service.class)
        this.folders = s.folders
    }
    
    public void clearServiceFolders() throws RibbitException {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }

        String userId = config.getActiveUserId()

        def req = new SignedRequest(config)
        def vars = new JSONObject()
        try {
            vars.put("folders", [ ] )
        } catch(JSONException ex) {
            throw new RibbitException("An unexpected error occured creating a JSONObject")
        }

        String uri = "services/${userId}/${id.toString().replace('\"','')}"
        String serviceResult = req.put(vars, uri);
        def obj =  new JsonParser().parse(serviceResult)

        def s = Utils.deserialize(obj.get("entry").toString(),Service.class)
        this.folders = s.folders
    }

    public void setAsVoicemailTranscriptionProvider(boolean status) {
         if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }

        String userId = config.getActiveUserId()

        def req = new SignedRequest(config)
        def vars = new JSONObject()
        try {
            vars.put("voicemail", status )
        } catch(JSONException ex) {
            throw new RibbitException("An unexpected error occured creating a JSONObject")
        }

        String uri = "services/${userId}/${id.toString().replace('\"','')}"
        String serviceResult = req.put(vars, uri);

        this.voicemail = status
    }
}
