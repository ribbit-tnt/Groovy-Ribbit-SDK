package com.ribbit.rest.deserializers

import com.ribbit.rest.Device
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 10, 2010
 * Time: 11:09:32 PM
 * To change this template use File | Settings | File Templates.
 */
class DeviceDeserializer implements JsonDeserializer<Device> {
    Device deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = jsonElement
        def device = new Device()
        device.id = obj.get("id").toString()
        device.name = obj.get("name").toString()
        device.carrier = obj.get("carrier").toString()

        ['verifed', 'callme', 'attachmessage', 'ringstatus',
                'callbackreachme', 'allowCCF', 'autoAnswer', 'usewave', 'showcalled', 'shared', 'mailtext'].each { name ->
            def o = obj.get(name)
            if (((JsonPrimitive)o)?.isBoolean()) {
                device.setProperty (name, o.asBoolean())
            }

        }
        return device
    }
}
