package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 12:09:36 PM
 */
enum MediaType {
    DIGITS('digits'),
    DURATION('duration'),
    FILE('file'),
    MONEY('money'),
    MONTH('month'),
    NUMBER('number'),
    RANK('rank'),
    TIME('time'),
    SPELL('spell'),
    WEEKDAY('weekday'),
    YEAR('year');

    private String value

    public MediaType(value) {
        this.value = value;
    }

    public String toString() {
        return value
    }
}
