package com.ribbit.rest.util;

import com.ribbit.rest.exceptions.RibbitException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A helper class used when signing messages
 */
public final class OAuthUtility {
    public static final String ENCODING = "UTF-8";
    public static final String HMAC_SHA1 = "HmacSHA1";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    /**
     * Percent encode a string
     * @param s a string to encode
     * @return an encoded string
     */
    public static String percentEncode(String s) {
        String result = "";
        try {
            if (s != null) {
                result = URLEncoder.encode(s, ENCODING).replace("%7E", "~").replace("*", "%2A").replace("+", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
    /**
     * Calculate the signature of a string
     * @param clearText the string to sign
     * @param key the key to sign with
     * @return the signature
     * @throws RibbitException in the unlikely event this fails
     */
    public static String calculateHMACSHA1(String clearText, String key)throws RibbitException {
        String result;
        RibbitException ex = new RibbitException("Failed to generated HMAC");
        try {
            byte[] keyBytes = key.getBytes(ENCODING);
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(clearText.getBytes(ENCODING));
            result = new String(Base64.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw ex;
        } catch (NoSuchAlgorithmException e) {
            throw ex;
        } catch (InvalidKeyException e) {
            throw ex;
        }
        return result;
    }

    /**
     * Calculate the signature of a string
     * @param clearText the body to sign
     * @param key the key to sign with
     * @return the signature
     * @throws RibbitException in the unlikely event this fails
     */
    public static String calculateHMACSHA1(byte[] body, String key)throws RibbitException {
        String result;
        RibbitException ex = new RibbitException("Failed to generated HMAC");
        try {
            byte[] keyBytes = key.getBytes(ENCODING);
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(body);
            result = new String(Base64.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw ex;
        } catch (NoSuchAlgorithmException e) {
            throw ex;
        } catch (InvalidKeyException e) {
            throw ex;
        }
        return result;
    }

    /**
     * Normalizes a URL
     * @param stringUrl a url to normalize
     * @return a normalized url
     * @throws RibbitException when a string that is not a url is passed in the stringUrl parameter
     */
    public static String normalizeURL(String stringUrl) throws RibbitException {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            throw new RibbitException("Failed to parse URL '" + stringUrl + "'");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(url.getProtocol().toLowerCase(Locale.ENGLISH));
        sb.append("://");
        sb.append(url.getHost());
        if (url.getPort() > 0
                && !((url.getProtocol().equalsIgnoreCase("http") && url
                      .getPort() == HTTP_PORT) || (url.getProtocol()
                                                   .equalsIgnoreCase("https") && url.getPort() == HTTPS_PORT))) {
            sb.append(":").append(url.getPort());
        }
        sb.append(url.getPath());
        return sb.toString();
    }

    /**
     * Used to sort query string parameters
     * @param fullUri the uri
     * @return the map of name value pairs
     * @throws RibbitException
     */
    public static SortedMap<String, NameValuePair> populateMapForQueryString(String fullUri) throws RibbitException {
        URL url = null;
        try {
            url = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RibbitException("Failed to parse URL '" + fullUri + "'");
        }
        String queryString = url.getQuery();
        TreeMap<String, NameValuePair> map = new TreeMap<String, NameValuePair>();
        if (queryString != null) {
            for (String paramPair : queryString.split("&")) {
                String[] nvp = paramPair.split("=");
                String name = nvp[0];
                String value = nvp.length > 1 ? nvp[1] : "";
                map.put(nvp[0], new NameValuePair(name, value));
            }
        }
        return map;
    }
}
