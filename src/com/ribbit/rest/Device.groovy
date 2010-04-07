package com.ribbit.rest

import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.oauth.SignedRequest

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 10, 2010
 * Time: 11:09:58 PM
 * To change this template use File | Settings | File Templates.
 */
class Device {
    String id
    String name
    String carrier
    boolean verified
    boolean callme
    boolean notifyvm
    boolean notifytranscription
    boolean attachmessage
    boolean usewave
    boolean callbackreachme
    boolean mailtext
    boolean shared
    boolean notifymissedcall
    boolean showcalled
    boolean ringstatus
    boolean autoAnswer
    boolean allowCCF

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

    public updateDevice() {
        
    }
}
