package com.ribbit.rest.deserializers;

import com.google.gson.*;
import com.ribbit.rest.Service;

import java.lang.reflect.Type
import com.ribbit.rest.constants.ServiceType

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 10:16:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceDeserializer implements JsonDeserializer<Service>{
    Service deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def obj = jsonElement
        def service = new Service()
        service.id = obj.get("id").toString()
        service.status = obj.get("status").toString()
        service.serviceType = obj.get("type").toString()

        def folders = obj.get("folders")
        if (folders instanceof JsonPrimitive) {
           def list = [folders.toString()]
            service.folders = list
        } else if (folders instanceof JsonArray) {
            def arr = (JsonArray)folders
            def list = [ ]
            arr.each {
                list.add(it)
            }
            service.folders = list.asImmutable()
        }


        ['voicemail', 'active'].each {name ->
            def o = obj.get(name)
            if (((JsonPrimitive) o)?.isBoolean()) {
                service.setProperty(name, o.asBoolean())
            }
        }
        return service
    }
}