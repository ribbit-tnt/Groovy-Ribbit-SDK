package com.ribbit.rest.util;

/**
 * A class to wrap a guery string name value pair
 */
public final class NameValuePair {

    private String name;
    private String value;

    /**
     * A constructor for a Name Value Pair
     * @param nvpName The key name
     * @param nvpValue The key value
     */
    public NameValuePair(String nvpName, String nvpValue) {
        name = nvpName;
        value = nvpValue;
    }

    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * returns 'name=value'
     */
    @Override
    public String toString() {
        String theString = this.getName().trim() + "=";
        if (getValue() != null)
            theString += this.getValue().trim();
        return theString;
    }

}
