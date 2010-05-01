package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 12:06:29 PM
 */
public enum Filter {
    BY_APP_ID('application.id'),
    BY_DOMAIN('application.domain.name'),  // really needed?
    BY_USER_ID('user.guid'),
    BY_FOLDER('folder'),
    BY_MEDIA_LOCATION('mediaLocation'),
    BY_DESTINATION('destination'),
    BY_MESSAGE_TYPE('messageType'),
    BY_NOTES('notes'),
    BY_SENDER('sender'),
    BY_STATUS('messageStatus'),
    BY_TAGS('tags'),
    BY_TITLE('title'),
    BY_UID('uid')

    private String value;


    public Filter(value) {
        this.value = value
    }


    public String toString() {
        return value
    }
}
