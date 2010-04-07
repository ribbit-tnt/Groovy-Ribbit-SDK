package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 1:24:21 PM
 */
public enum MessageStatus {
    DELETED("DELETED"),
    DELIVERED("DELIVERED"),
    INITIAL("INITIAL"),
    FAILED("FAILED"),
    NEW("NEW"),
    READ("READ"),
    RECEIVED("RECEIVED"),
    SENT("SENT"),
    UNKNOWN("UNKNOWN"),
    URGENT("URGENT");

    private String value

    public MessageStatus(value) {
        this.value = value;
    }

    public String toString() {
        return value
    }
}
