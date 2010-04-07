package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 12:05:24 PM
 */
public enum Voice {
   EN_US('en_US/classic'),
   EN_UK('en_UK/classic');

   private String value


    public Voice(value) {
        this.value = value;
    }

    public String toString() {
        return value
    }
}
