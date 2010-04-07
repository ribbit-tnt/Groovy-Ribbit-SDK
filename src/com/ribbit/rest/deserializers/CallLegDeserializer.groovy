package com.ribbit.rest.deserializers

import com.ribbit.rest.CallLeg
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import com.google.gson.JsonDeserializationContext
import org.joda.time.format.DateTimeFormat
import com.ribbit.rest.constants.CallStatus
import org.joda.time.tz.CachedDateTimeZone
import com.google.gson.JsonPrimitive

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 4:38:40 PM
 * To change this template use File | Settings | File Templates.
 */
/*
def id
    CallStatus status
    DateTime startTime
    DateTime answerTime
    DateTime endTime
    String duration
    String mode
    String announce
    boolean playing
    boolean recording
 */


class CallLegDeserializer implements JsonDeserializer<CallLeg>{
    static parser = DateTimeFormat.forPattern('yyyy-MM-ddHH:mm:ss')

    CallLeg deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = jsonElement
        def callLeg = new CallLeg()
        callLeg.id = obj.get('id')
        def status = ((String)obj.get('status')).replace('\"', "")
        callLeg.status = Enum.valueOf(CallStatus.class, status)

        callLeg.duration = obj?.get("duration")
        def date = obj?.get('startTime')
        if (date != null)                                           // can be null
            callLeg.startTime = parser.parseDateTime(date.asString -"T"-"Z").toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)
        date = obj.get('endTime')
        if (date != null)                                          // can be null
            callLeg.endTime = parser.parseDateTime(date.asString -"T"-"Z").toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)
        date = obj?.get('answerTime')
        if (date != null)                                          // can be null
            callLeg.answerTime = parser.parseDateTime(date.asString -"T"-"Z").toLocalDateTime().toDateTime(CachedDateTimeZone.UTC)

        ['playing','recording'].each { name ->
            def o = obj?.get(name)
            if (((JsonPrimitive)o)?.isBoolean() && o != null) {
                callLeg.setProperty (name, o)
            }

        }

        return callLeg
    }

}
