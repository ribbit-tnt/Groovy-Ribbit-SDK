package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 3:49:17 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MessageFilter {
     BY_DESTINATION("destination"),
     BY_FOLDER("folder"),
     BY_MEDIA_LOCATION("mediaLocation"),
     BY_MESSAGE_TYPE("messageType"),
     BY_NOTES("notes"),
     BY_SENDER("sender"),
     BY_STATUS("messageStatus"),
     BY_TAGS("tags"),
     BY_TITLE("title"),
     BY_TRANSCRIPTION_STATUS("transcriptionStatus"),
     BY_USER_ID("uid");

     private String value


    public MessageFilter(value) {
        this.value = value;
    }


    public String toString() {
        return value
    }
}