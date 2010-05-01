package com.ribbit.rest

import com.google.gson.JsonParser
import com.ribbit.rest.deserializers.Utils
import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.exceptions.RibbitException
import com.ribbit.rest.oauth.SignedRequest
import com.ribbit.rest.util.Util
import com.ribbit.rest.util.json.JSONException
import com.ribbit.rest.util.json.JSONObject

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 10, 2010
 * Time: 11:09:58 PM
 * To change this template use File | Settings | File Templates.
 */
class Device extends Resource {
    String id, label, name, carrier, key, verifyBy
    Boolean verified, callme, notifyvm, notifytranscription, attachmessage, usewave
    Boolean callbackreachme, mailtext, shared, notifymissedcall, answersecurity
    Boolean showcalled, ringstatus, autoAnswer, allowCCF

    boolean isDirty

    public Device(map) {
        try {
            map.each {HashMap.Entry entry ->
                this.setProperty(entry.getKey().toString(), entry.getValue())
            }
        } catch (Exception ex) {
            ex.printStackTrace()
        }
    }

    public removeDevice() {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        def req = new SignedRequest(config)
        String uri = "devices/${userId}/${id}"
        signedRequest.delete(uri)

        return true
    }

    public create() {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        def exceptions = []

        [id: id, name: name].each {
            if (!Util.isValidString(it.value)) {
                exceptions.add("${it.key} is required")
            }
        }
        [label: label, key: key, verifyBy: verifyBy].each {
            if (!Util.isValidStringIfDefined(it.value)) {
                exceptions.add("When defined, ${it.key} must be a string of one or more characters")
            }
        }

        [callme: callme, notifyvm: notifyvm, callbackreachme: callbackreachme, mailtext: mailtext, showcalled: showcalled,
                answersecurity: answersecurity, notifytranscription: notifytranscription, attachmessage: attachmessage,
                usewave: usewave, autoAnswer: autoAnswer, allowCCF: allowCCF, ringstatus: ringstatus
        ].each {
            if (!Util.isBoolIfDefined(it.value)) {
                exceptions.add("When defined, ${it.key} must be boolean")
            }
        }

        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"))
        }

        def vars = new JSONObject()
        try {
            vars.put("id", id);
            vars.put("name", name);

            [label: label, callme: callme, notifyvm: notifyvm, callbackreachme: callbackreachme, mailtext: mailtext, showcalled: showcalled,
                    answersecurity: answersecurity, notifytranscription: notifytranscription, attachmessage: attachmessage,
                    usewave: usewave, autoAnswer: autoAnswer, allowCCF: allowCCF, ringstatus: ringstatus, key: key, verifyBy: verifyBy
            ].each {
                if (Util.isDefined(it.key)) {
                    vars.put(it.key, it.value)
                }
            }

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured creating a JSONObject")
        }
        def req = new SignedRequest(config)
        String uriToCall = "devices/${userId}"
        String serviceResult = req.post(vars, uriToCall)

        return Util.getIdFromUri(serviceResult);
    }

    public update(map) {
        if (config.getAccountId() == null) {
            throw new NotAuthorizedException()
        }
        String userId = config.getActiveUserId()

        def exceptions = []


        [id: map?.id, name: map?.name].each {
            if (!Util.isValidString(it.value)) {
                exceptions.add("${it.key} is required")
            }
        }
        [label: map?.label, key: map?.key, verifyBy: map?.verifyBy].each {
            if (!Util.isValidStringIfDefined(it.value)) {
                exceptions.add("When defined, ${it.key} must be a string of one or more characters")
            }
        }

        [callme: map?.callme, notifyvm: map?.notifyvm, callbackreachme: map?.callbackreachme, mailtext: map?.mailtext, showcalled: map?.showcalled,
                answersecurity: map?.answersecurity, notifytranscription: map?.notifytranscription, attachmessage: map?.attachmessage,
                usewave: map?.usewave, autoAnswer: map?.autoAnswer, allowCCF: map?.allowCCF, ringstatus: map?.ringstatus
        ].each {
            if (!Util.isBoolIfDefined(it.value)) {
                exceptions.add("When defined, ${it.key} must be boolean")
            }
        }

        def vars = new JSONObject()
        try {
            vars.put("id", id);
            vars.put("name", name);

            [label: map?.label, callme: map?.callme, notifyvm: map?.notifyvm, callbackreachme: map?.callbackreachme, mailtext: map?.mailtext, showcalled: map?.showcalled,
                    answersecurity: map?.answersecurity, notifytranscription: map?.notifytranscription, attachmessage: map?.attachmessage,
                    usewave: map?.usewave, autoAnswer: map?.autoAnswer, allowCCF: map?.allowCCF, ringstatus: map?.ringstatus, key: map?.key, verifyBy: map?.verifyBy
            ].each {
                if (Util.isDefined(it.key)) {
                    vars.put(it.key, it.value)
                }
            }

        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured creating a JSONObject")
        }

        def req = new SignedRequest(config)
        String uri = "devices/${userId}/${id}"
        String serviceResult = req.put(vars, uri)

        try {
            def obj = new JsonParser().parse(serviceResult).getAt("entry")
            def result = Utils.deserialize(obj.toString(), Device.class)
            return result
        } catch (JSONException e) {
            throw new RibbitException("An unexpected error occured parsing the response " + e.getMessage());
        }

    }
}
