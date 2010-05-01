package com.ribbit.rest.deserializers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ribbit.rest.Folder
import com.ribbit.rest.FolderResource
import com.ribbit.rest.Device
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken
import com.ribbit.rest.Service
import com.ribbit.rest.User
import com.ribbit.rest.Call
import com.ribbit.rest.CallLeg
import com.ribbit.rest.Message
import com.ribbit.rest.MessageDestination

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 4, 2010
 * Time: 4:13:46 PM
 */
class Utils {
    static Gson gson

    static {
        GsonBuilder builder = new GsonBuilder();
            builder.registerDeserializer(Folder.class, new FolderDeserializer())
            builder.registerDeserializer(Device.class, new DeviceDeserializer())
            builder.registerDeserializer(Service.class, new ServiceDeserializer())
            builder.registerDeserializer(User.class, new UserDeserializer())
            builder.registerDeserializer(Call.class, new CallDeserializer())
            builder.registerDeserializer(CallLeg.class, new CallLegDeserializer())
            builder.registerDeserializer(FolderResource.class, new FolderResourceDeserializer())
            builder.registerDeserializer(Message.class , new MessageDeserializer())
            gson = builder.create()
    }

    static deserialize(json, clazz) {
         return gson.fromJson(json, clazz)   
    }

    static getGson() {
        gson
    }
}
