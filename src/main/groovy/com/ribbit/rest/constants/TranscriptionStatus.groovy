package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 3:52:36 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TranscriptionStatus {
    FAILED("notAvailable"),
    PENDING("pending"),
    TRANSCRIBED("transcribed");

    private String value;


    public TranscriptionStatus(value) {
        this.value = value;
    }

    public String toString() {
        return value
    }
}