package com.ribbit.rest.util.json;

public interface IToJSON {
    JSONObject objectToJsonCollection() throws JSONException;
    JSONObject objectToJsonCollection(String name) throws JSONException;
}