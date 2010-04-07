package com.ribbit.rest.deserializers

import com.ribbit.rest.User
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonElement
import java.lang.reflect.Type
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonArray
import org.joda.time.tz.CachedDateTimeZone
import org.joda.time.format.DateTimeFormat

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 3:07:46 PM
 * To change this template use File | Settings | File Templates.
 */

/*
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
*/

class UserDeserializer implements JsonDeserializer<User>{
    static parser = DateTimeFormat.forPattern('yyyy-MM-ddHH:mm:ss')

    User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = jsonElement
        def user = new User()
        user.login = obj.get("login").toString()
        user.domain = obj.get("domain").toString()

        user.firstName = obj.get("firstName").toString()
        user.lastName = obj.get("lastName").toString()
        user.status = obj.get("status").toString()
        user.callerId = obj.get("callerId").toString()
        user.login = obj.get("login").toString()
        user.createdWith = obj.get("createdWith")

        user.suspended = obj.get("type").asBoolean()
        def date = obj.get('createdOn').asString -"T"-"Z"
        user.createdOn = parser.parseDateTime(date).toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)
        return user
    }
}

