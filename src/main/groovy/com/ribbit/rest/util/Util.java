package com.ribbit.rest.util;

import com.ribbit.rest.exceptions.RibbitException;
import com.ribbit.rest.util.json.JSONArray;
import com.ribbit.rest.util.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility functions used by the Ribbit Java library
 */
public final class Util {

    private static String XML_DATE_FORMAT= "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static String ACCOUNT_DATE_FORMAT= "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Join an array
     * @param strings The strings to join
     * @param separator A separator
     * @return
     */
    public static String join(ArrayList<String> strings, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < strings.size(); i++) {
            if (i != 0) sb.append(separator);
            sb.append(strings.get(i));
        }
        return sb.toString();
    }

    /**
     * Converts a JSON object into a string list, if possible
     * @param obj a JSON object containing a value of 'field', expected to be null, a string value, or a JSONArray containing strings
     * @param field the name of the field to parse into a string list.
     * @return
     */
    public static List<String> makeStringArray(JSONObject obj, String field) {
        ArrayList<String> out = new ArrayList<String>();
        JSONArray tmpArray = obj.optJSONArray(field);
        if (tmpArray != null) {
            for (int i = 0; i < tmpArray.length(); i++) {
                out.add((String)tmpArray.opt(i));
            }
        } else {
            String s = obj.optString(field);
            if (s != null) {
                out.add(s);
            }
        }
        return out;
    }

    /**
     * Extract a Telephone number from a Device ID
     * @param id a device Id
     * @return a telephone number
     */
    public static String getInboundNumberFromId(String id) {
        int i = id.lastIndexOf(":");
        String fullPhoneNumber = id.substring(i + 1);
        return fullPhoneNumber.replace("+", "");
    }

    /**
     * Extract a string ID from a URI
     * @param uri the URI of a created resource
     * @return an ID
     */
    public static String getIdFromUri(String uri) {
        int i = uri.lastIndexOf("/");
        return uri.substring(i+1);
    }

    /**
     * Extract a long ID from a URI
     * @param uri the URI of a created resource
     * @return an ID
     */
    public static Long getIdFromUrit(String uri) {
        int i = uri.lastIndexOf("/");
        return new Long(uri.substring(i+1));
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isValidString(String value) {
        return value != null && value.length() > 0;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isValidStringIfDefined(String value) {
        return value == null || isValidString(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isValidDate(Date value) {
        return value != null;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isValidDateIfDefined(Date value) {
        return value == null || isValidDate(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isBoolIfDefined(Boolean value) {
        return true;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveInteger(Integer value) {
        return value != null && value >= 0;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveInteger(Long value) {
        return value != null && value >= 0;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveInteger(int value) {
        return value >= 0;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveInteger(long value) {
        return value >= 0;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return if parameter valid, otherwise false
     */
    public static boolean isPositiveIntegerIfDefined(Integer value) {
        return value == null || isPositiveInteger(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveIntegerIfDefined(Long value) {
        return value == null || isPositiveInteger(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveIntegerIfDefined(int value) {
        return isPositiveInteger(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isPositiveIntegerIfDefined(long value) {
        return isPositiveInteger(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isNonEmptyArray(List<String> value) {
        return value != null && !value.isEmpty();
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isNonEmptyArrayIfDefined (List<String> value) {
        return value == null || isNonEmptyArray(value);
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isDefined(String value) {
        return value != null && value.length() > 0;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isDefined(Object value) {
        return value != null;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isDefined(Integer value) {
        return value != null;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isDefined(int value) {
        return true;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter valid, otherwise false
     */
    public static boolean isDefined(long value) {
        return true;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if parameter value is of type List<?>
     */
    public static boolean isArray(Object value) {
        return value != null && value instanceof List<?>;
    }

    /**
     * Used for parameter validation
     * @param value a value to validate
     * @return true if value is null or of type List<?>
     */
    public static boolean isArrayIfDefined(Object value) {
        return value == null || value instanceof List<?>;
    }

    /**
     * Formats a date for sending to the Ribbit Rest Server
     * @param date a date to format
     * @return true if parameter valid, otherwise false
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(XML_DATE_FORMAT);
        return formatter.format(date);
    }

    /**
     * Parse a string sent from the Ribbit Server into a Java Date
     * @param dateAsString a string containing a date
     * @return a Date
     * @throws RibbitException if an unparseable string is passed in
     */
    public static Date parseAccountDate(String dateAsString) throws RibbitException {
        SimpleDateFormat formatter = new SimpleDateFormat(ACCOUNT_DATE_FORMAT);
        Date date = null;
        if (dateAsString != null && dateAsString.length() > 0 ) {
            try {
                date = formatter.parse(dateAsString);
            } catch (ParseException ex) {
                throw new RibbitException("There was a problem parsing a date string of " + dateAsString);
            }
        }
        return date;
    }

    /**
     * Parse a string sent from the Ribbit Server into a Java Date
     * @param dateAsString a string containing a date
     * @return a Date
     * @throws RibbitException if an unparseable string is passed in
     */
    public static Date parseDate(String dateAsString) throws RibbitException {
        SimpleDateFormat formatter = new SimpleDateFormat(XML_DATE_FORMAT);
        Date date = null;
        if (dateAsString != null && dateAsString.length() > 0 ) {
            try {
                date = formatter.parse(dateAsString);
            } catch (ParseException ex) {
                throw new RibbitException("There was a problem parsing a date string of " + dateAsString);
            }
        }
        return date;
    }

    /**
     * Formats a date into a string useable in a URI
     * @param date a date to format
     * @return an appropriately formatted string
     */
    public static String formatDateForUri(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(date);
    }


    /**
     * Validates paging parameters and returns error messages as appropriate.
     *
     * @param startIndex the start index
     * @param count the count
     * @return an exception string if the values don't validate
     */
    public static String checkPagingParameters(Integer startIndex, Integer count) {
        ArrayList<String> exceptions = new ArrayList<String>();
        if (startIndex != null && count == null) {
            exceptions.add("If startIndex is specified, count must be specified too");
        }
        if (startIndex == null && count != null) {
            exceptions.add("If count is specified, startIndex must be specified too");
        }
        if (startIndex != null && count != null) {
            if (!isPositiveInteger(startIndex)) {
                exceptions.add("startIndex must be a positive integer");
            }
            if (!isPositiveInteger(count)) {
                exceptions.add("count must be a positive integer");
            }
        }

        return exceptions.size() > 0 ? join(exceptions,";") : null;
    }

    /**
     *  Validates filtering parameters and returns error messages as appropriate.
     *
     * @param filterBy the field to filter by
     * @param filterValue the value to filter
     * @return an exception string if the values don't validate
     */
    public static String checkFilterParameters(String filterBy, String filterValue) {
        ArrayList<String> exceptions = new ArrayList<String>();

        if (filterBy != null && filterValue == null) {
            exceptions.add("If filterBy is specified, filterValue must be specified too");
        }
        if (filterBy == null && filterValue != null) {
            exceptions.add("If filterValue is specified, filterBy must be specified too");
        }
        if (filterBy != null && filterValue != null) {
            //if (!isValidStringIfDefined(x)) { exceptions.add("startIndex must be a positive integer"); }
            if (!isValidStringIfDefined(filterValue)) {
                exceptions.add("When defined, filterValue  must be a string of one or more characters");
            }
        }

        return exceptions.size() > 0 ? join(exceptions,";") : null;
    }

    /**
     * Creates a string with paging details
     * @param startIndex the first result to fetch
     * @param count the number of results to fetch
     * @return a query string fragment
     */
    public static String createPagingQueryString(Integer startIndex, Integer count) {
        return (count != null) ? "?" + createPagingInnerString(startIndex, count) : "";
    }


    private static String createPagingInnerString(Integer startIndex, Integer count) {
        return "startIndex=" + startIndex.toString() + "&count=" + count.toString();
    }

    /**
     * Creates a string with filtering details
     * @param filterBy the index to filter on
     * @param filterValue the value to filter with
     * @return a query string fragment
     */
    public static String createFilteringQueryString(String filterBy, String filterValue)throws RibbitException {
        return (filterBy != null) ? "?" + createFilteringInnerString(filterBy, filterValue) : "";
    }

    private static String createFilteringInnerString(String filterBy, String filterValue) throws RibbitException {
        return "filterBy=" + filterBy + "&filterValue=" + filterValue;
    }

    /**
     * Creates a string with filtering and paging details
     * @param startIndex the first result to fetch
     * @param count the number of results to fetch
     * @param filterBy the index to filter on
     * @param filterValue the value to filter with
     * @return a query string fragment
     */
    public static String createQueryString(Integer startIndex, Integer count, String filterBy, String filterValue) throws RibbitException {
        String result = createPagingQueryString(startIndex, count);
        if (result.length() > 0 && filterBy != null) {
            result = result + "&" + createFilteringInnerString(filterBy, filterValue);
        } else if (filterBy != null) {
            result = createFilteringQueryString(filterBy, filterValue);
        }
        return result;
    }

    /**
     * Creates a redirect uri for the OAuth Approval journey.
     * @param uri a uri to be redirected back to from the OAuth Approval journey.
     * @return A redirect uri.
     */
    public static String redirectUriBuilder(String uri) {
        String result = uri;
        if (uri.contains("oauth_approval")) {
            if (uri.contains("?oauth_approval=approved&")) {
                result = uri.replace("oauth_approval=approved&", "");
            } else if (uri.contains("?oauth_approval=denied&")) {
                result = uri.replace("oauth_approval=denied&", "");
            } else if (uri.contains("?oauth_approval=approved")) {
                result = uri.replace("?oauth_approval=approved", "");
            } else if (uri.contains("?oauth_approval=denied")) {
                result = uri.replace("?oauth_approval=denied", "");
            } else if (uri.contains("oauth_approval=approved")) {
                result = uri.replace("&oauth_approval=approved", "");
            } else if (uri.contains("oauth_approval=denied")) {
                result = uri.replace("&oauth_approval=denied", "");
            }
        }
        return result;
    }

}
