package com.ribbit.rest.util;

/**
 * A type of event sent from the Ribbit Rest Server to your applications notificationUrl
 */
public enum CallBackNotificationTypes {
    /**
     * A call or leg has been answered
     */
    CALL_ANSWERED,
    /**
     * A call has been costed
     */
    CALL_COST,
    /**
     * A call has failed
     */
    CALL_GENERAL_FAILURE,
    /**
     * A call or leg has been hung up
     */
    CALL_HANGUP,
    /**
     * A new call is incoming
     */
    CALL_INCOMING,
    /**
     * A call or leg has been moved
     */
    CALL_MOVED,
    /**
     * A call or leg has failed to answer
     */
    CALL_NOT_ANSWERED,
    /**
     * A call or leg has gone on hold
     */
    CALL_ON_HOLD,
    /**
     * A call or leg has reached voicemail
     */
    CALL_REACHED_VOICEMAIL,
    /**
     * A call or leg has resumed
     */
    CALL_RESUMED,
    /**
     * A call or leg is ringing
     */
    CALL_RINGING,
    /**
     * A call or leg has stopped ringing
     */
    CALL_STOPPED_RINGING,
    /**
     * A call or leg has been transferred
     */
    CALL_TRANSFERRED,
    /**
     * The applications credit has all gone
     */
    CREDIT_ALL_GONE,
    /**
     * The application can no longer use credit
     */
    CREDIT_EXHAUSTED,
    /**
     * A message has been deleted
     */
    MESSAGE_DELETED,
    /**
     * A new call log entry
     */
    NEW_CALL_LOG,
    /**
     * A new voicemail has been left
     */
    NEW_VOICEMAIL,
    /**
     * A transcription has completed
     */
    TRANSCRIPTION_DATA,
}
