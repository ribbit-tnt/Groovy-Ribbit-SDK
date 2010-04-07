package com.ribbit.rest.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.ribbit.rest.FolderResource
import java.lang.reflect.Type
import org.joda.time.format.DateTimeFormat
import org.joda.time.tz.CachedDateTimeZone

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 4, 2010
 * Time: 3:56:55 PM
 */
class FolderResourceDeserializer  implements JsonDeserializer<FolderResource> {
    static parser = DateTimeFormat.forPattern('yyyy-MM-ddHH:mm:ss')
    FolderResource deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = (JsonObject)jsonElement

        def rsc = new FolderResource()
        rsc.id = obj.get('id').asString
        rsc.associatedApp = obj.get('createdWith')
        rsc.createdBy = obj.get('createdBy')
        def date = obj.getAsJsonPrimitive('createdOn').asString -"T"-"Z"
        rsc.createdOn = parser.parseDateTime(date).toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)
        def rUsers = obj.getAsJsonArray("readUsers")
        rUsers.each {
            rsc.readUsers.add(it.toString())
        }

        def wUsers = obj.getAsJsonArray("writeUsers")
        wUsers.each {
            rsc.writeUsers.add(it.toString())
        }

        return rsc
    }

}
