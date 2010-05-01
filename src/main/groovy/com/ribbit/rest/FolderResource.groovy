package com.ribbit.rest

import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 4, 2010
 * Time: 3:35:04 PM
 */
class FolderResource {

    String id
    String associatedApp
    String createdBy
    DateTime createdOn
    List readUsers  = new ArrayList<String>()
    List writeUsers = new ArrayList<String>()
}
