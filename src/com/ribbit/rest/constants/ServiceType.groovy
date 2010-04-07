package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 10:31:28 AM
 * To change this template use File | Settings | File Templates.
 */
public enum ServiceType {
    TRANSCRIPTION("Transcription")

    private String value

    public ServiceType(value) {
        this.value = value;
    }

    public String toString() {
        return value
    }
}
