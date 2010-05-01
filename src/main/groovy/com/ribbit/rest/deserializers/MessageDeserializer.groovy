package com.ribbit.rest.deserializers

import com.ribbit.rest.Message
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonPrimitive
import com.google.gson.JsonArray
import com.ribbit.rest.MessageDestination
import org.joda.time.format.DateTimeFormat
import org.joda.time.tz.CachedDateTimeZone

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Apr 26, 2010
 * Time: 2:33:01 PM
 * To change this template use File | Settings | File Templates.
 */
class MessageDeserializer implements JsonDeserializer<Message> {
    static parser = DateTimeFormat.forPattern('yyyy-MM-ddHH:mm:ss')

    Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = jsonElement
        def message = new Message()
        message.id = obj.get("id").toString()
        message.title = obj.get("title").toString()
        message.userId = obj.get("userId").toString()
        message.sender = obj.get("sender").toString()
        message.from = obj.get("from").toString()
        message.mediaUri = obj.get("mediaUri").toString()
        message.time = parser.parseDateTime(obj.get("time").asString - "T" - "Z").toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)
        message.folder = obj.get("folder").toString()
        message.body = obj.get("body").toString()

        ['newMessage', 'urgentMessage'].each { name ->
            def o = obj.get(name)
            if (((JsonPrimitive)o)?.isBoolean()) {
                message.setProperty (name, o.asBoolean())
            }

        }

        JsonArray array = obj.get("mediaItems")
        array.each {
            message.mediaItems.add(it.toString())       
        }

        def subElements = (JsonArray) (obj.get("recipients"))
        subElements.each {
            def dest = new MessageDestination(destination:it.get("destination"), status:it.get("status"))
            message.recipients.add(dest)
        }

        return message
    }
}