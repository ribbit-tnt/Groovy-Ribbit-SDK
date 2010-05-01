package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 3:54:06 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MessageType {
    BROADCAST_VOICEMAIL("BroadcastVoiceMail"),
    EMAIL("email"),
    INBOUND_AUDIO_MESSAGE("InboundAudioMessage"),
    INBOUND_SMS("InboundSms"),
    OUTBOUND_AUDIO_MESSAGE("OutboundAudioMessage"),
    OUTBOUND_SMS("OutboundSms"),
    SMS("sms"),
    VOICEMAIL("Voicemail");

    private String value;

    public MessageType(value) {
        this.value = value;
    }

    public String toString() {
        return value
    }
}