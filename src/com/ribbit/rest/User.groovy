package com.ribbit.rest

import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 3:04:27 PM
 * To change this template use File | Settings | File Templates.
 */
class User extends Resource {
     String id
     String login
     String domain
     String firstName
     String lastName
     boolean suspended
     String status
     String createdWith
     String pwdStatus
     Long accountId
     DateTime createdOn
     String callerId
}
