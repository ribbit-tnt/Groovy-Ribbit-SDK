package com.ribbit.rest.constants

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 12:06:29 PM
 */
public enum Filter {
    static final BY_APP_ID = 'application.id'
    static final BY_DOMAIN = 'application.domain.name'  // really needed?
    static final BY_USER_ID = 'user.guid'
    static final BY_FOLDER = 'folder'
    static final BY_MEDIA_LOCATION = 'mediaLocation'
    static final BY_DESTINATION = 'destination'
    static final BY_MESSAGE_TYPE = 'messageType'
    static final BY_NOTES = 'notes'
    static final BY_SENDER = 'sender'
    static final BY_STATUS = 'messageStatus'
    static final BY_TAGS = 'tags'
    static final BY_TITLE = 'title'
    static final BY_UID = 'uid'
}
