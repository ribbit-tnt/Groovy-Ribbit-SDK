package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Apr 7, 2010
 * Time: 2:51:02 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Locale {
    USA("USA"),
    GBR("GBR");

    private String value

    public Locale(String value) {
        this.value = value
    }

    public String toString() {
        return value
    }
}