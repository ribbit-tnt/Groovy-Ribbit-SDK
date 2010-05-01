package com.ribbit.rest.deserializers

import com.ribbit.rest.Folder
import com.ribbit.rest.FolderResource
import java.lang.reflect.Type
import com.google.gson.*

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 4, 2010
 * Time: 3:40:11 PM
 */
class FolderDeserializer implements JsonDeserializer<Folder> {

    Folder deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        def folder = new Folder()
        def subElements = (JsonArray)(((JsonObject)jsonElement).get("entry"))
        //.getAsJsonArray()
        def rscSerializer = new FolderResourceDeserializer()
        subElements.each {
           folder.files.add(rscSerializer.deserialize(it, FolderResource.class, jsonDeserializationContext))
        }

        return folder
    }
}
