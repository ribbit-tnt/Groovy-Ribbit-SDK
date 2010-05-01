package com.ribbit.rest.deserializers

import com.ribbit.rest.Call
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import com.google.gson.JsonDeserializationContext
import org.joda.time.format.DateTimeFormat
import org.joda.time.tz.CachedDateTimeZone
import com.google.gson.JsonPrimitive
import com.google.gson.JsonArray
import com.ribbit.rest.CallLeg

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 4:08:03 PM
 * To change this template use File | Settings | File Templates.
 */
class CallDeserializer implements JsonDeserializer<Call> {
    static parser = DateTimeFormat.forPattern('yyyy-MM-ddHH:mm:ss')

    Call deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = jsonElement
        def call = new Call()
        println obj.get("id")
        call.callID = obj.get("id")
        call.duration = obj.get("duration")
        def date = obj?.get('startTime')
        if (date != null)                                           // can be null
            call.startTime = parser.parseDateTime(date.asString - "T" - "Z").toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)
        date = obj.get('endTime')
        if (date != null)                                          // can be null
            call.endTime = parser.parseDateTime(date.asString - "T" - "Z").toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)

        // TODO recordings

        ['success', 'active', 'recording', 'outbound'].each {name ->
            def o = obj.get(name)
            if (((JsonPrimitive) o)?.isBoolean() && o != null) {
                call.setProperty(name, o)
            }

        }

        def subElements = (JsonArray) (obj.get("legs"))
        def callLegSerializer = new CallLegDeserializer()
        subElements.each {
            call.legs.add(callLegSerializer.deserialize(it, CallLeg.class, jsonDeserializationContext))
        }

        return call
    }

}
